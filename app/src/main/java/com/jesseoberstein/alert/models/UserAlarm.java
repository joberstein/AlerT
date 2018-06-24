package com.jesseoberstein.alert.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.jesseoberstein.alert.BR;
import com.jesseoberstein.alert.models.mbta.BaseResource;
import com.jesseoberstein.alert.models.mbta.Direction;
import com.jesseoberstein.alert.models.mbta.Endpoint;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.models.mbta.Stop;
import com.jesseoberstein.alert.utils.AlarmUtils;
import com.jesseoberstein.alert.utils.Constants;
import com.jesseoberstein.alert.utils.DateTimeUtils;
import com.jesseoberstein.alert.validation.Validatable;

import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Objects.isNull;

@Entity(
    tableName = "user_alarms",
    indices = {@Index("route_id"), @Index("stop_id"), @Index("repeat_type_id")}
)
public class UserAlarm extends BaseObservable implements Serializable, Validatable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "route_id")
    @ForeignKey(entity = Route.class, parentColumns = "id", childColumns = "route_id")
    private String routeId;

    @ColumnInfo(name = "direction_id")
    @ForeignKey(entity = Direction.class, parentColumns = "id", childColumns = "direction_id")
    private long directionId;

    @ColumnInfo(name = "stop_id")
    @ForeignKey(entity = Stop.class, parentColumns = "id", childColumns = "stop_id")
    private String stopId;

    @ColumnInfo(name = "repeat_type_id")
    @ForeignKey(entity = RepeatType.class, parentColumns = "id", childColumns = "repeat_type_id")
    private long repeatTypeId;

    @Embedded
    private SelectedDays selectedDays;

    private Integer hour;
    private Integer minutes;
    private String nickname;
    private long duration;
    private boolean active;

    @Ignore
    private String time;

    @Ignore
    private RepeatType repeatType;

    @Ignore
    private String nextFiringDayString;

    @Ignore
    private Route route;

    @Ignore
    private Stop stop;

    @Ignore
    private Direction direction;

    @Ignore
    private List<Endpoint> endpoints;

    @Ignore
    private List<String> errors = new ArrayList<>();

    public UserAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, 1);

        setTime(calendar.get(HOUR_OF_DAY), calendar.get(MINUTE));
        setRepeatType(RepeatType.NEVER);
        setSelectedDays(new SelectedDays());
        setDuration(30);
        setEndpoints(new ArrayList<>());
        setActive(true);
    }

    public UserAlarm(UserAlarm alarm) {
        setId(alarm.getId());
        setRoute(alarm.getRoute());
        setStop(alarm.getStop());
        setNickname(alarm.getNickname());
        setDirection(alarm.getDirection());
        setEndpoints(alarm.getEndpoints());
        setTime(alarm.getHour(), alarm.getMinutes());
        setDuration(alarm.getDuration());
        setRepeatType(alarm.getRepeatType());
        setSelectedDays(alarm.getSelectedDays());
        setActive(alarm.isActive());
    }

    @Bindable
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        notifyPropertyChanged(BR.nickname);
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    @Bindable
    public SelectedDays getSelectedDays() {
        return selectedDays;
    }

    public void setSelectedDays(SelectedDays selectedDays) {
        this.selectedDays = selectedDays;
        notifyPropertyChanged(BR.selectedDays);
        setNextFiringDayString();
    }

    public void setSelectedDays(int[] selectedDays) {
        this.selectedDays = new SelectedDays(selectedDays);
        setNextFiringDayString();
    }

    @Bindable
    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
        notifyPropertyChanged(BR.duration);
    }

    public long getRepeatTypeId() {
        return repeatTypeId;
    }

    public void setRepeatTypeId(long repeatTypeId) {
        this.repeatTypeId = repeatTypeId;
    }

    @Bindable
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        notifyPropertyChanged(BR.active);
    }

    @Bindable
    public String getNextFiringDayString() {
        return this.nextFiringDayString;
    }

    public void setNextFiringDayString() {
        if (this.getSelectedDays() == null) {
            return;
        }

        String[] weekdayList = DateFormatSymbols.getInstance().getWeekdays();

        long now = DateTimeUtils.getCurrentTimeInMillis();
        long alarmFiringTime = DateTimeUtils.getTimeInMillis(this.hour, this.minutes);
        boolean isPastAlarmFiringTime = alarmFiringTime <= now;

        int nextFiringDay = AlarmUtils.getNextFiringDay(this);
        String nextFiringDayString = weekdayList[nextFiringDay];
        String firingDayTodayString = isPastAlarmFiringTime ? "Next " + nextFiringDayString : "Today";
        boolean isNextFiringDayToday = DateTimeUtils.getCurrentDay() == nextFiringDay;

        this.nextFiringDayString = isNextFiringDayToday ? firingDayTodayString : nextFiringDayString;
        notifyPropertyChanged(BR.nextFiringDayString);
    }

    @Bindable
    public String getTime() {
        if (this.hour == null || this.minutes == null) {
            return null;
        }

        setTime(this.hour, this.minutes);
        return this.time;

    }

    public void setTime(Integer newHour, Integer newMinutes) {
        if (newHour == null || newMinutes == null) {
            return;
        }

        if (!newHour.equals(this.hour)) {
            setHour(newHour);
        }

        if (!newMinutes.equals(this.minutes)) {
            setMinutes(newMinutes);
        }

        this.time = DateTimeUtils.getFormattedTime(this.hour, this.minutes);
        notifyPropertyChanged(BR.time);
        setNextFiringDayString();
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public long getDirectionId() {
        return directionId;
    }

    public void setDirectionId(long directionId) {
        this.directionId = directionId;
    }

    @Bindable
    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
        String routeId = Optional.ofNullable(route).map(BaseResource::getId).orElse("");
        setRouteId(routeId);
        notifyPropertyChanged(BR.route);
    }

    @Bindable
    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        long directionId = Optional.ofNullable(direction).map(Direction::getId).orElse(-1L);
        setDirectionId(directionId);
        notifyPropertyChanged(BR.direction);
    }

    @Bindable
    public Stop getStop() {
        return stop;
    }

    public void setStop(Stop stop) {
        this.stop = stop;
        String stopId = Optional.ofNullable(stop).map(Stop::getId).orElse("");
        setStopId(stopId);
        notifyPropertyChanged(BR.stop);
    }

    @Bindable
    public List<Endpoint> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<Endpoint> endpoints) {
        this.endpoints = endpoints;
        notifyPropertyChanged(BR.endpoints);
    }

    @Bindable
    public RepeatType getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(RepeatType repeatType) {
        if (repeatType == this.repeatType) {
            return;
        }

        this.repeatType = repeatType;
        setRepeatTypeId(repeatType.getId());
        setSelectedDays(repeatType.getSelectedDays());

        notifyPropertyChanged(BR.repeatType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAlarm userAlarm = (UserAlarm) o;

        if (id != userAlarm.id) return false;
        if (directionId != userAlarm.directionId) return false;
        if (repeatTypeId != userAlarm.repeatTypeId) return false;
        if (duration != userAlarm.duration) return false;
        if (active != userAlarm.active) return false;
        if (routeId != null ? !routeId.equals(userAlarm.routeId) : userAlarm.routeId != null)
            return false;
        if (stopId != null ? !stopId.equals(userAlarm.stopId) : userAlarm.stopId != null)
            return false;
        if (selectedDays != null ? !selectedDays.equals(userAlarm.selectedDays) : userAlarm.selectedDays != null)
            return false;
        if (hour != null ? !hour.equals(userAlarm.hour) : userAlarm.hour != null) return false;
        if (minutes != null ? !minutes.equals(userAlarm.minutes) : userAlarm.minutes != null)
            return false;
        if (nickname != null ? !nickname.equals(userAlarm.nickname) : userAlarm.nickname != null)
            return false;
        return endpoints != null ? endpoints.equals(userAlarm.endpoints) : userAlarm.endpoints == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (routeId != null ? routeId.hashCode() : 0);
        result = 31 * result + (int) (directionId ^ (directionId >>> 32));
        result = 31 * result + (stopId != null ? stopId.hashCode() : 0);
        result = 31 * result + (int) (repeatTypeId ^ (repeatTypeId >>> 32));
        result = 31 * result + (selectedDays != null ? selectedDays.hashCode() : 0);
        result = 31 * result + (hour != null ? hour.hashCode() : 0);
        result = 31 * result + (minutes != null ? minutes.hashCode() : 0);
        result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
        result = 31 * result + (int) (duration ^ (duration >>> 32));
        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + (endpoints != null ? endpoints.hashCode() : 0);
        result = 31 * result + (errors != null ? errors.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserAlarm{" +
                "id=" + id +
                ", routeId='" + routeId + '\'' +
                ", directionId=" + directionId +
                ", stopId='" + stopId + '\'' +
                ", repeatTypeId=" + repeatTypeId +
                ", selectedDays=" + selectedDays +
                ", hour=" + hour +
                ", minutes=" + minutes +
                ", nickname='" + nickname + '\'' +
                ", duration=" + duration +
                ", active=" + active +
                ", time='" + time + '\'' +
                ", repeatType=" + repeatType +
                ", nextFiringDayString='" + nextFiringDayString + '\'' +
                ", route=" + route +
                ", stop=" + stop +
                ", direction=" + direction +
                ", endpoints=" + endpoints + '\'' +
                '}';
    }

    @Override
    public boolean isValid() {
        this.errors.clear();

        if (RepeatType.CUSTOM.getId() == this.repeatTypeId && !this.getSelectedDays().isAnyDaySelected()) {
            this.errors.add(Constants.CUSTOM_REPEAT_TYPE);
        }
        if (isNull(this.routeId) || this.routeId.isEmpty()) {
            this.errors.add(Constants.ROUTE);
        }
        if (isNull(this.directionId) || this.directionId < 0) {
            this.errors.add(Constants.DIRECTION_ID);
        }
        if (isNull(this.stopId) || this.stopId.isEmpty()) {
            this.errors.add(Constants.STOP_ID);
        }
        if (isNull(this.endpoints) || this.endpoints.isEmpty()) {
            this.errors.add(Constants.ENDPOINTS);
        }

        return this.errors.isEmpty();
    }

    @Override
    public List<String> getErrors() {
        return this.errors;
    }
}