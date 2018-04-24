package com.jesseoberstein.alert.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.jesseoberstein.alert.BR;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.models.mbta.Stop;
import com.jesseoberstein.alert.utils.AlarmUtils;
import com.jesseoberstein.alert.utils.DateTimeUtils;

import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;

@DatabaseTable(tableName = "user_alarms")
public class UserAlarm extends BaseObservable implements Serializable {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true)
    private Route route;

    @DatabaseField
    private String nickname;

    @DatabaseField
    private String direction;

    @DatabaseField(foreign = true)
    private Stop stop;

    @Deprecated
    private String station;

    @DatabaseField
    private Integer hour;

    @DatabaseField
    private Integer minutes;

    private SelectedDays selectedDays;

    @DatabaseField
    private long duration;

    @DatabaseField
    private RepeatType repeatType;

    @DatabaseField
    private boolean active;

    private String time;

    private String nextFiringDayString;

    public UserAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, 1);

        setTime(calendar.get(HOUR_OF_DAY), calendar.get(MINUTE));
        setRepeatType(RepeatType.NEVER);
        setSelectedDays(new SelectedDays());
        setDuration(30);
    }

    public UserAlarm(UserAlarm alarm) {
        setId(alarm.getId());
        setRoute(alarm.getRoute());
        setNickname(alarm.getNickname());
        setDirection(alarm.getDirection());
        setStation(alarm.getStation());
        setTime(alarm.getHour(), alarm.getMinutes());
        setSelectedDays(alarm.getSelectedDays());
        setDuration(alarm.getDuration());
        setRepeatType(alarm.getRepeatType());
        setActive(alarm.isActive());
    }

    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
        notifyPropertyChanged(BR.route);
        setStop(null);
    }

    @Bindable
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        notifyPropertyChanged(BR.nickname);
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Deprecated
    public String getStation() {
        return station;
    }

    @Deprecated
    public void setStation(String station) {
        this.station = station;
    }

    @Bindable
    public Stop getStop() {
        return stop;
    }

    public void setStop(Stop stop) {
        this.stop = stop;
        notifyPropertyChanged(BR.stop);
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

    public SelectedDays getSelectedDays() {
        return selectedDays;
    }

    public void setSelectedDays(SelectedDays selectedDays) {
        this.selectedDays = selectedDays;
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

    @Bindable
    public RepeatType getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(RepeatType repeatType) {
        if (repeatType.equals(this.repeatType)) {
            return;
        }

        this.repeatType = repeatType;
        notifyPropertyChanged(BR.repeatType);

        setSelectedDays(repeatType.getSelectedDays());
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

        return time;
    }

    public void setTime(Integer newHour, Integer newMinutes) {
        if (newHour == null || newMinutes == null) {
            return;
        }

        // If the time is the same as it was before, no need to update the time.
        if (newHour.equals(this.hour) && newMinutes.equals(this.minutes)) {
            return;
        }

        setHour(newHour);
        setMinutes(newMinutes);
        this.time = DateTimeUtils.getFormattedTime(this.hour, this.minutes);
        notifyPropertyChanged(BR.time);

        setNextFiringDayString();
    }

    @Override
    public String toString() {
        return "UserAlarm{" +
                "id=" + id +
                ", route=" + route +
                ", nickname='" + nickname + '\'' +
                ", direction='" + direction + '\'' +
                ", station='" + station + '\'' +
                ", hour=" + hour +
                ", minutes=" + minutes +
                ", selectedDays=" + selectedDays +
                ", duration=" + duration +
                ", repeatType='" + repeatType + '\'' +
                ", active=" + active +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAlarm userAlarm = (UserAlarm) o;

        if (id != userAlarm.id) return false;
        if (duration != userAlarm.duration) return false;
        if (active != userAlarm.active) return false;
        if (route != null ? !route.equals(userAlarm.route) : userAlarm.route != null) return false;
        if (nickname != null ? !nickname.equals(userAlarm.nickname) : userAlarm.nickname != null)
            return false;
        if (direction != null ? !direction.equals(userAlarm.direction) : userAlarm.direction != null)
            return false;
        if (stop != null ? !stop.equals(userAlarm.stop) : userAlarm.stop != null) return false;
        if (station != null ? !station.equals(userAlarm.station) : userAlarm.station != null)
            return false;
        if (hour != null ? !hour.equals(userAlarm.hour) : userAlarm.hour != null) return false;
        if (minutes != null ? !minutes.equals(userAlarm.minutes) : userAlarm.minutes != null)
            return false;
        if (selectedDays != null ? !selectedDays.equals(userAlarm.selectedDays) : userAlarm.selectedDays != null)
            return false;
        if (repeatType != userAlarm.repeatType) return false;
        if (time != null ? !time.equals(userAlarm.time) : userAlarm.time != null) return false;
        return nextFiringDayString != null ? nextFiringDayString.equals(userAlarm.nextFiringDayString) : userAlarm.nextFiringDayString == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (route != null ? route.hashCode() : 0);
        result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        result = 31 * result + (stop != null ? stop.hashCode() : 0);
        result = 31 * result + (station != null ? station.hashCode() : 0);
        result = 31 * result + (hour != null ? hour.hashCode() : 0);
        result = 31 * result + (minutes != null ? minutes.hashCode() : 0);
        result = 31 * result + (selectedDays != null ? selectedDays.hashCode() : 0);
        result = 31 * result + (int) (duration ^ (duration >>> 32));
        result = 31 * result + (repeatType != null ? repeatType.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (nextFiringDayString != null ? nextFiringDayString.hashCode() : 0);
        return result;
    }
}