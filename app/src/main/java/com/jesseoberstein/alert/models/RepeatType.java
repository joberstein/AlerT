package com.jesseoberstein.alert.models;

import androidx.annotation.IntDef;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

public class RepeatType implements Serializable {
    private static final int never = 0;
    private static final int weekdays = 1;
    private static final int weekends = 2;
    private static final int daily = 3;
    private static final int custom = 4;

    public static final RepeatType NEVER = new RepeatType(never);
    public static final RepeatType WEEKDAYS = new RepeatType(weekdays);
    public static final RepeatType WEEKENDS = new RepeatType(weekends);
    public static final RepeatType DAILY = new RepeatType(daily);
    public static final RepeatType CUSTOM = new RepeatType(custom);

    private static final RepeatType[] VALUES = new RepeatType[]{NEVER, WEEKDAYS, WEEKENDS, DAILY, CUSTOM};

    private @RepeatTypeEnum int repeatType;

    private RepeatType(@RepeatTypeEnum int repeatType) {
        this.repeatType = repeatType;
    }

    @IntDef({never, weekdays, weekends, daily, custom})
    @Retention(RetentionPolicy.SOURCE)
    private @interface RepeatTypeEnum {}

    public int getId() {
        return this.repeatType;
    }

    public SelectedDays getSelectedDays() {
        switch (this.repeatType) {
            case weekdays:
                return SelectedDays.WEEKDAYS;
            case weekends:
                return SelectedDays.WEEKENDS;
            case daily:
                return SelectedDays.DAILY;
            default:
                return SelectedDays.DEFAULT;
        }
    }

    public static String[] getRepeatTypes() {
        return Arrays.stream(VALUES)
                .sorted(Comparator.comparing(RepeatType::getId))
                .map(RepeatType::toString)
                .collect(Collectors.toList())
                .toArray(new String[]{});
    }

    public static RepeatType[] values() {
        return VALUES;
    }

    public static RepeatType valueOf(int id) {
        switch (id) {
            case never:
                return NEVER;
            case weekdays:
                return WEEKDAYS;
            case weekends:
                return WEEKENDS;
            case daily:
                return DAILY;
            case custom:
                return CUSTOM;
            default:
                return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RepeatType that = (RepeatType) o;

        return repeatType == that.repeatType;
    }

    @Override
    public int hashCode() {
        return repeatType;
    }

    @Override
    public String toString() {
        switch (this.repeatType) {
            case never:
                return "Never";
            case weekdays:
                return "Mon - Fri";
            case weekends:
                return "Sat - Sun";
            case daily:
                return "Daily";
            case custom:
                return "Custom";
            default:
                return "Unknown";
        }
    }
}