package com.jesseoberstein.alert.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.mbta.Direction;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.models.mbta.Stop;
import com.jesseoberstein.alert.utils.AlertUtils;

import java.util.Optional;
import java.util.function.Function;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@HiltViewModel
@EqualsAndHashCode(callSuper = false)
public class DraftAlarmViewModel extends ViewModel {

    AppDatabase database;
    SavedStateHandle state;

    @Inject
    public DraftAlarmViewModel(SavedStateHandle savedStateHandle, AppDatabase database) {
        this.state = savedStateHandle;
        this.database = database;
        this.addUserAlarmSources();
    }

    MutableLiveData<UserAlarm> draftAlarm = new MutableLiveData<>();
    MutableLiveData<String> nickname = new MutableLiveData<>();
    MutableLiveData<Route> route = new MutableLiveData<>();
    MutableLiveData<Stop> stop = new MutableLiveData<>();
    MutableLiveData<Direction> direction = new MutableLiveData<>();
    MediatorLiveData<UserAlarm> draftAlarmChanged = new MediatorLiveData<>();
    LiveData<Integer> themeId = Transformations.map(this.route, AlertUtils::getTheme);

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

    private void addUserAlarmSources() {
        addUserAlarmSource(this.draftAlarm, alarm -> alarm);
        addUserAlarmSource(this.nickname, nickname -> getUserAlarm().withNickname(nickname));
        addUserAlarmSource(this.route, route -> this.mergeRoute(route, getUserAlarm()));
        addUserAlarmSource(this.direction, direction -> this.mergeDirection(direction, getUserAlarm()));
        addUserAlarmSource(this.stop, stop -> this.mergeStop(stop, getUserAlarm()));
    }

    private UserAlarm mergeRoute(Route route, UserAlarm userAlarm) {
        return Optional.ofNullable(route)
                .map(Route::getId)
                .map(userAlarm::withRouteId)
                .orElse(userAlarm);
    }

    private UserAlarm mergeDirection(Direction direction, UserAlarm userAlarm) {
        return Optional.ofNullable(direction)
                .map(Direction::getId)
                .map(userAlarm::withDirectionId)
                .orElse(userAlarm);
    }

    private UserAlarm mergeStop(Stop stop, UserAlarm userAlarm) {
        return Optional.ofNullable(stop)
                .map(Stop::getId)
                .map(userAlarm::withStopId)
                .orElse(userAlarm);
    }

    private <T> void addUserAlarmSource(LiveData<T> liveData, Function<T, UserAlarm> merge) {
        this.draftAlarmChanged.addSource(liveData, data -> {
            UserAlarm mergedAlarm = merge.apply(data);
            this.draftAlarmChanged.setValue(mergedAlarm);
        });
    }

    private UserAlarm getUserAlarm() {
        UserAlarm existing = this.draftAlarmChanged.getValue();
        return Optional.ofNullable(existing).orElse(new UserAlarm());
    }
}
