package com.jesseoberstein.alert.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.models.RepeatType;
import com.jesseoberstein.alert.models.SelectedDays;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.mbta.Direction;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.models.mbta.Stop;
import com.jesseoberstein.alert.utils.AlertUtils;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.EqualsAndHashCode;
import lombok.Value;

import static java.time.temporal.TemporalAdjusters.next;
import static java.time.temporal.TemporalAdjusters.nextOrSame;

@Value
@HiltViewModel
@EqualsAndHashCode(callSuper = false)
public class DraftAlarmViewModel extends ViewModel {

    private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);

    AppDatabase database;
    SavedStateHandle state;

    @Inject
    public DraftAlarmViewModel(SavedStateHandle savedStateHandle, AppDatabase database) {
        this.state = savedStateHandle;
        this.database = database;

        this.addTimeSources();
        this.addNextFiringDaysSources();
        this.addUserAlarmSources();
    }

    MutableLiveData<Integer> hour = new MutableLiveData<>();
    MutableLiveData<Integer> minutes = new MutableLiveData<>();
    MutableLiveData<String> nickname = new MutableLiveData<>();
    MutableLiveData<RepeatType> repeatType = new MutableLiveData<>();
    MutableLiveData<SelectedDays> selectedDays = new MutableLiveData<>();
    MutableLiveData<Duration> duration = new MutableLiveData<>();
    MutableLiveData<Route> route = new MutableLiveData<>();
    MutableLiveData<Stop> stop = new MutableLiveData<>();
    MutableLiveData<Direction> direction = new MutableLiveData<>();

    MediatorLiveData<LocalTime> time = new MediatorLiveData<>();
    MediatorLiveData<LocalDateTime> nextFiringDay = new MediatorLiveData<>();
    MediatorLiveData<UserAlarm> draftAlarm = new MediatorLiveData<>();

    LiveData<Integer> themeId = Transformations.map(this.route, AlertUtils::getTheme);
    LiveData<String> formattedTime = Transformations.map(this.time, DATE_TIME_FORMATTER::format);
    LiveData<String> formattedNextFiringDay = Transformations.map(this.nextFiringDay, this::formatNextFiringDay);

    public void loadRoute(String routeId) {
        this.database.routeDao().get(routeId)
                .subscribeOn(Schedulers.io())
                .subscribe(this.route::postValue);
    }

    public void loadStop(String stopId, String routeId, int directionId) {
        this.database.stopDao().get(stopId, routeId, directionId)
                .subscribeOn(Schedulers.io())
                .subscribe(this.stop::postValue);
    }

    public void loadDirection(long directionId, String routeId) {
        this.database.directionDao().get(directionId, routeId)
                .subscribeOn(Schedulers.io())
                .subscribe(this.direction::postValue);
    }

    private UserAlarm getCurrentUserAlarm() {
        UserAlarm existing = this.draftAlarm.getValue();
        return Optional.ofNullable(existing).orElse(new UserAlarm());
    }

    private void addUserAlarmSources() {
        addSource(this.hour, this.draftAlarm,
                hour -> getCurrentUserAlarm().withHour(hour));

        addSource(this.minutes, this.draftAlarm,
                minutes -> getCurrentUserAlarm().withMinutes(minutes));

        addSource(this.nickname, this.draftAlarm,
                nickname -> getCurrentUserAlarm().withNickname(nickname));

        addSource(this.repeatType, this.draftAlarm,
                repeatType -> getCurrentUserAlarm().withRepeatType(repeatType));

        addSource(this.selectedDays, this.draftAlarm,
                selectedDays -> getCurrentUserAlarm().withSelectedDays(selectedDays));

        addSource(this.duration, this.draftAlarm,
                duration -> getCurrentUserAlarm().withDuration(duration.toMinutes()));

        addSource(this.route, this.draftAlarm,
                route -> getCurrentUserAlarm().withRoute(route));

        addSource(this.direction, this.draftAlarm,
                direction -> getCurrentUserAlarm().withDirection(direction));

        addSource(this.stop, this.draftAlarm,
                stop -> getCurrentUserAlarm().withStop(stop));
    }

    private LocalTime getCurrentTime() {
        LocalTime existing = this.time.getValue();
        return Optional.ofNullable(existing).orElse(LocalTime.now().plusHours(1));
    }

    private void addTimeSources() {
        addSource(this.hour, this.time, hour -> getCurrentTime().withHour(hour));
        addSource(this.minutes, this.time, minutes -> getCurrentTime().withMinute(minutes));
    }

    private void addNextFiringDaysSources() {
        addSource(this.time, this.nextFiringDay, time -> {
            SelectedDays existingDays = this.getSelectedDays().getValue();
            SelectedDays days = Optional.ofNullable(existingDays).orElse(new SelectedDays());
            return getNextFiringDay(time, days);
        });

        addSource(this.selectedDays, this.nextFiringDay, selectedDays -> {
            LocalTime existingTime = this.getTime().getValue();
            LocalTime time = Optional.ofNullable(existingTime).orElse(LocalTime.now().plusHours(1));
            return getNextFiringDay(time, selectedDays);
        });
    }

    private LocalDateTime getNextFiringDay(LocalTime time, SelectedDays selectedDays) {
        boolean[] selectedArray = selectedDays.toBooleanArray();
        LocalTime now = LocalTime.now();
        LocalDateTime todayAtTime = LocalDate.now().atTime(time);

        return Arrays.stream(DayOfWeek.values())
                .filter(dayOfWeek -> selectedArray[dayOfWeek.getValue()-1])
                .map(dayOfWeek -> time.isAfter(now) ? nextOrSame(dayOfWeek) : next(dayOfWeek))
                .map(todayAtTime::with)
                .sorted()
                .findFirst()
                .orElseGet(() -> time.isAfter(now) ? todayAtTime : todayAtTime.plusDays(1));
    }

    private String formatNextFiringDay(LocalDateTime day) {
        LocalDateTime now = LocalDate.now().atStartOfDay();
        Duration durationBetween = Duration.between(now, day.toLocalDate().atStartOfDay());
        int daysAway = Long.valueOf(durationBetween.toDays()).intValue();
        String dayOfWeek = day.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.US);

        switch (daysAway) {
            case 0:
                return "Today";
            case 1:
                return "Tomorrow";
            case 7:
                return "Next " + dayOfWeek;
            default:
                return dayOfWeek;
        }
    }

    private <S,T> void addSource(LiveData<S> source, MediatorLiveData<T> mediator, Function<S,T> merge) {
        mediator.addSource(source, data -> {
            T merged = merge.apply(data);
            mediator.setValue(merged);
        });
    }
}
