package com.jesseoberstein.alert.models;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

import static com.jesseoberstein.alert.utils.Constants.DAYS;
import static com.jesseoberstein.alert.utils.Constants.DURATION;
import static com.jesseoberstein.alert.utils.Constants.DURATION_TYPE;
import static com.jesseoberstein.alert.utils.Constants.HOUR;
import static com.jesseoberstein.alert.utils.Constants.IS_ACTIVE;
import static com.jesseoberstein.alert.utils.Constants.MINUTES;
import static com.jesseoberstein.alert.utils.Constants.NICKNAME;
import static com.jesseoberstein.alert.utils.Constants.REPEAT;

@DatabaseTable(tableName = "user_alarms")
public class UserAlarm implements Parcelable {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true)
    private UserRoute route;

    @DatabaseField
    private String nickname;

    @DatabaseField
    private String station;

    @DatabaseField
    private int hour;

    @DatabaseField
    private int minutes;

    @DatabaseField
    private boolean monday;

    @DatabaseField
    private boolean tuesday;

    @DatabaseField
    private boolean wednesday;

    @DatabaseField
    private boolean thursday;

    @DatabaseField
    private boolean friday;

    @DatabaseField
    private boolean saturday;

    @DatabaseField
    private boolean sunday;

    @DatabaseField
    private int duration;

    @DatabaseField(columnName = "duration_type")
    private String durationType;

    @DatabaseField
    private String repeat;

    @DatabaseField
    private boolean active;

    public UserAlarm() {}

    private UserAlarm(Parcel in) {
        id = in.readInt();
        nickname = in.readString();
        station = in.readString();
        hour = in.readInt();
        minutes = in.readInt();
        duration = in.readInt();
        durationType = in.readString();
        repeat = in.readString();
        active = in.readInt() == 1;
        setWeekdays(in.createStringArrayList());
    }

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

    public boolean isMonday() {
        return monday;
    }

    public void setMonday(boolean monday) {
        this.monday = monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public void setTuesday(boolean tuesday) {
        this.tuesday = tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public void setWednesday(boolean wednesday) {
        this.wednesday = wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public void setThursday(boolean thursday) {
        this.thursday = thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public void setFriday(boolean friday) {
        this.friday = friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public void setSaturday(boolean saturday) {
        this.saturday = saturday;
    }

    public boolean isSunday() {
        return sunday;
    }

    public void setSunday(boolean sunday) {
        this.sunday = sunday;
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

    public void setTime(Bundle bundle) {
        setHour(bundle.getInt(HOUR));
        setMinutes(bundle.getInt(MINUTES));
    }

    public void setDays(Bundle bundle) {
        setWeekdays(bundle.getStringArrayList(DAYS));
    }

    public void setSettings(Bundle bundle) {
        setNickname(bundle.getString(NICKNAME));
        setDuration(bundle.getInt(DURATION));
        setDurationType(bundle.getString(DURATION_TYPE));
        setRepeat(bundle.getString(REPEAT));
        setActive(bundle.getBoolean(IS_ACTIVE));
    }

    public void setWeekdays(List<String> weekdays) {
        weekdays.forEach(weekday -> {
            switch (weekday) {
                case "Monday":
                    setMonday(true);
                    break;
                case "Tuesday":
                    setTuesday(true);
                    break;
                case "Wednesday":
                    setWednesday(true);
                    break;
                case "Thursday":
                    setTuesday(true);
                    break;
                case "Friday":
                    setFriday(true);
                    break;
                case "Saturday":
                    setSaturday(true);
                    break;
                case "Sunday":
                    setSunday(true);
            }
        });
    }

    public ArrayList<String> getWeekdays() {
        ArrayList<String> weekdays = new ArrayList<>();

        if (isMonday()) {
            weekdays.add("Monday");
        }
        if (isTuesday()) {
            weekdays.add("Tuesday");
        }
        if (isWednesday()) {
            weekdays.add("Wednesday");
        }
        if (isThursday()) {
            weekdays.add("Thursday");
        }
        if (isFriday()) {
            weekdays.add("Friday");
        }
        if (isSaturday()) {
            weekdays.add("Saturday");
        }
        if (isSunday()) {
            weekdays.add("Sunday");
        }

        return weekdays;
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(nickname);
        out.writeString(station);
        out.writeInt(hour);
        out.writeInt(minutes);
        out.writeInt(duration);
        out.writeString(durationType);
        out.writeString(repeat);
        out.writeInt((active ? 1 : 0));
        out.writeStringList(getWeekdays());
    }

    public static final Parcelable.Creator<UserAlarm> CREATOR = new Parcelable.Creator<UserAlarm>() {
        public UserAlarm createFromParcel(Parcel in) {
            return new UserAlarm(in);
        }

        public UserAlarm[] newArray(int size) {
            return new UserAlarm[size];
        }
    };
}