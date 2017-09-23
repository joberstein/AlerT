package com.jesseoberstein.alert.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.stream.IntStream;

import static android.icu.util.Calendar.FRIDAY;
import static android.icu.util.Calendar.MONDAY;
import static android.icu.util.Calendar.SATURDAY;
import static android.icu.util.Calendar.SUNDAY;
import static android.icu.util.Calendar.THURSDAY;
import static android.icu.util.Calendar.TUESDAY;
import static android.icu.util.Calendar.WEDNESDAY;

@DatabaseTable(tableName = "user_alarms")
public class UserAlarm implements Serializable {

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
    private int hour;

    @DatabaseField
    private int minutes;

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
    private int duration;

    @DatabaseField(columnName = "duration_type")
    private String durationType;

    @DatabaseField
    private String repeat;

    @DatabaseField
    private boolean active;

    public UserAlarm() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserRoute getRoute() {
        return route;
    }

    public void setRoute(UserRoute route) {
        this.route = route;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDurationType() {
        return durationType;
    }

    public void setDurationType(String durationType) {
        this.durationType = durationType;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setWeekdays(int[] weekdays) {
        IntStream.range(SUNDAY, SATURDAY + 1).forEach(i -> {
            int isSelected = weekdays[i];

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
    }

    public int[] getWeekdays() {
        int[] weekdays = new int[SATURDAY + 1];
        weekdays[0] = -1; // should not be used; Calendar days are indices 1 - 7.

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

        return weekdays;
    }

    private int getMonday() {
        return monday;
    }

    private void setMonday(int monday) {
        this.monday = monday;
    }

    private int getTuesday() {
        return tuesday;
    }

    private void setTuesday(int tuesday) {
        this.tuesday = tuesday;
    }

    private int getWednesday() {
        return wednesday;
    }

    private void setWednesday(int wednesday) {
        this.wednesday = wednesday;
    }

    private int getThursday() {
        return thursday;
    }

    private void setThursday(int thursday) {
        this.thursday = thursday;
    }

    private int getFriday() {
        return friday;
    }

    private void setFriday(int friday) {
        this.friday = friday;
    }

    private int getSaturday() {
        return saturday;
    }

    private void setSaturday(int saturday) {
        this.saturday = saturday;
    }

    private int getSunday() {
        return sunday;
    }

    private void setSunday(int sunday) {
        this.sunday = sunday;
    }

    @Override
    public String toString() {
        return "UserAlarm{" +
                "id=" + id +
                ", route=" + route +
                ", nickname='" + nickname + '\'' +
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
                ", durationType='" + durationType + '\'' +
                ", repeat='" + repeat + '\'' +
                ", active=" + active +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAlarm userAlarm = (UserAlarm) o;

        if (id != userAlarm.id) return false;
        if (hour != userAlarm.hour) return false;
        if (minutes != userAlarm.minutes) return false;
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
        if (durationType != null ? !durationType.equals(userAlarm.durationType) : userAlarm.durationType != null)
            return false;
        return repeat != null ? repeat.equals(userAlarm.repeat) : userAlarm.repeat == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (route != null ? route.hashCode() : 0);
        result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        result = 31 * result + (station != null ? station.hashCode() : 0);
        result = 31 * result + hour;
        result = 31 * result + minutes;
        result = 31 * result + monday;
        result = 31 * result + tuesday;
        result = 31 * result + wednesday;
        result = 31 * result + thursday;
        result = 31 * result + friday;
        result = 31 * result + saturday;
        result = 31 * result + sunday;
        result = 31 * result + duration;
        result = 31 * result + (durationType != null ? durationType.hashCode() : 0);
        result = 31 * result + (repeat != null ? repeat.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
        return result;
    }
}