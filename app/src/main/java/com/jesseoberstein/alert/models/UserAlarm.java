package com.jesseoberstein.alert.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.jesseoberstein.alert.BR;
import com.jesseoberstein.alert.utils.AlarmUtils;
import com.jesseoberstein.alert.utils.DateTimeUtils;

import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.stream.IntStream;

import static java.util.Calendar.FRIDAY;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONDAY;
import static java.util.Calendar.SATURDAY;
import static java.util.Calendar.SUNDAY;
import static java.util.Calendar.THURSDAY;
import static java.util.Calendar.TUESDAY;
import static java.util.Calendar.WEDNESDAY;

@DatabaseTable(tableName = "user_alarms")
public class UserAlarm extends BaseObservable implements Serializable {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true)
    private UserRoute route;

    @DatabaseField
    private String nickname;

    @DatabaseField
    private String direction;

    @DatabaseField
    private String station;

    @DatabaseField
    private Integer hour;

    @DatabaseField
    private Integer minutes;

    @DatabaseField
    private int monday;

    @DatabaseField
    private int tuesday;

    @DatabaseField
    private int wednesday;

    @DatabaseField
    private int thursday;

    @DatabaseField
    private int friday;

    @DatabaseField
    private int saturday;

    @DatabaseField
    private int sunday;

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
        setDuration(30);
    }

    public UserAlarm(UserAlarm alarm) {
        setId(alarm.getId());
        setRoute(alarm.getRoute());
        setNickname(alarm.getNickname());
        setDirection(alarm.getDirection());
        setStation(alarm.getStation());
        setTime(alarm.getHour(), alarm.getMinutes());
        setWeekdays(alarm.getWeekdays());
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

    public UserRoute getRoute() {
        return route;
    }

    public void setRoute(UserRoute route) {
        this.route = route;
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

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
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

        setWeekdays(repeatType.getSelectedDays());
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setWeekdays(int[] weekdays) {
        if (weekdays.length != 7) {
            String msg = "Could not set weekdays for alarm: given array has " + weekdays.length + " elements; should have exactly 7.";
            throw new RuntimeException(msg);
        }

        IntStream.range(1, SATURDAY + 1).forEach(i -> {
            // arr[i - 1] because weekdays has only 7 elements, but Calendar dates use indices 1-7.
            int isSelected = weekdays[i - 1];

            switch (i) {
                case SUNDAY:
                    setSunday(isSelected);
                    break;
                case MONDAY:
                    setMonday(isSelected);
                    break;
                case TUESDAY:
                    setTuesday(isSelected);
                    break;
                case WEDNESDAY:
                    setWednesday(isSelected);
                    break;
                case THURSDAY:
                    setThursday(isSelected);
                    break;
                case FRIDAY:
                    setFriday(isSelected);
                    break;
                case SATURDAY:
                    setSaturday(isSelected);
            }
        });

        setNextFiringDayString();
    }

    public int[] getWeekdays() {
        int[] weekdays = new int[SATURDAY + 1];
        Arrays.fill(weekdays, 0);

        IntStream.range(SUNDAY, SATURDAY + 1).forEach(i -> {
            switch (i) {
                case SUNDAY:
                    weekdays[SUNDAY] = getSunday();
                    break;
                case MONDAY:
                    weekdays[MONDAY] = getMonday();
                    break;
                case TUESDAY:
                    weekdays[TUESDAY] = getTuesday();
                    break;
                case WEDNESDAY:
                    weekdays[WEDNESDAY] = getWednesday();
                    break;
                case THURSDAY:
                    weekdays[THURSDAY] = getThursday();
                    break;
                case FRIDAY:
                    weekdays[FRIDAY] = getFriday();
                    break;
                case SATURDAY:
                    weekdays[SATURDAY] = getSaturday();
            }
        });

        return Arrays.copyOfRange(weekdays, 1, weekdays.length);
    }

    public int getMonday() {
        return monday;
    }

    public void setMonday(int monday) {
        this.monday = monday;
    }

    public int getTuesday() {
        return tuesday;
    }

    public void setTuesday(int tuesday) {
        this.tuesday = tuesday;
    }

    public int getWednesday() {
        return wednesday;
    }

    public void setWednesday(int wednesday) {
        this.wednesday = wednesday;
    }

    public int getThursday() {
        return thursday;
    }

    public void setThursday(int thursday) {
        this.thursday = thursday;
    }

    public int getFriday() {
        return friday;
    }

    public void setFriday(int friday) {
        this.friday = friday;
    }

    public int getSaturday() {
        return saturday;
    }

    public void setSaturday(int saturday) {
        this.saturday = saturday;
    }

    public int getSunday() {
        return sunday;
    }

    public void setSunday(int sunday) {
        this.sunday = sunday;
    }

    @Bindable
    public String getNextFiringDayString() {
        return this.nextFiringDayString;
    }

    public void setNextFiringDayString() {
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

        Date date = new Date();
        date.setTime(DateTimeUtils.getTimeInMillis(this.hour, this.minutes));

        this.time = new SimpleDateFormat("h:mm a", Locale.ENGLISH).format(date).toLowerCase();
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
                ", monday=" + monday +
                ", tuesday=" + tuesday +
                ", wednesday=" + wednesday +
                ", thursday=" + thursday +
                ", friday=" + friday +
                ", saturday=" + saturday +
                ", sunday=" + sunday +
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
        if (monday != userAlarm.monday) return false;
        if (tuesday != userAlarm.tuesday) return false;
        if (wednesday != userAlarm.wednesday) return false;
        if (thursday != userAlarm.thursday) return false;
        if (friday != userAlarm.friday) return false;
        if (saturday != userAlarm.saturday) return false;
        if (sunday != userAlarm.sunday) return false;
        if (duration != userAlarm.duration) return false;
        if (active != userAlarm.active) return false;
        if (route != null ? !route.equals(userAlarm.route) : userAlarm.route != null) return false;
        if (nickname != null ? !nickname.equals(userAlarm.nickname) : userAlarm.nickname != null)
            return false;
        if (direction != null ? !direction.equals(userAlarm.direction) : userAlarm.direction != null)
            return false;
        if (station != null ? !station.equals(userAlarm.station) : userAlarm.station != null)
            return false;
        if (hour != null ? !hour.equals(userAlarm.hour) : userAlarm.hour != null) return false;
        if (minutes != null ? !minutes.equals(userAlarm.minutes) : userAlarm.minutes != null)
            return false;
        if (repeatType != userAlarm.repeatType) return false;
        return time != null ? time.equals(userAlarm.time) : userAlarm.time == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (route != null ? route.hashCode() : 0);
        result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        result = 31 * result + (station != null ? station.hashCode() : 0);
        result = 31 * result + (hour != null ? hour.hashCode() : 0);
        result = 31 * result + (minutes != null ? minutes.hashCode() : 0);
        result = 31 * result + monday;
        result = 31 * result + tuesday;
        result = 31 * result + wednesday;
        result = 31 * result + thursday;
        result = 31 * result + friday;
        result = 31 * result + saturday;
        result = 31 * result + sunday;
        result = 31 * result + (int) (duration ^ (duration >>> 32));
        result = 31 * result + (repeatType != null ? repeatType.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }
}