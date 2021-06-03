package com.jesseoberstein.alert.models;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;

@With
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SelectedDays implements Serializable {
    private static final int SUNDAY = Calendar.SUNDAY - 1;
    private static final int MONDAY = Calendar.MONDAY - 1;
    private static final int TUESDAY = Calendar.TUESDAY - 1;
    private static final int WEDNESDAY = Calendar.WEDNESDAY - 1;
    private static final int THURSDAY = Calendar.THURSDAY - 1;
    private static final int FRIDAY = Calendar.FRIDAY - 1;
    private static final int SATURDAY = Calendar.SATURDAY - 1;

    private int monday;
    private int tuesday;
    private int wednesday;
    private int thursday;
    private int friday;
    private int saturday;
    private int sunday;

    public static final SelectedDays DEFAULT = new SelectedDays();

    public static final SelectedDays WEEKDAYS = SelectedDays.builder()
            .monday(1)
            .tuesday(1)
            .wednesday(1)
            .thursday(1)
            .friday(1)
            .build();

    public static final SelectedDays WEEKENDS = SelectedDays.builder()
            .saturday(1)
            .sunday(1)
            .build();

    public static final SelectedDays DAILY = SelectedDays.builder()
            .sunday(1)
            .monday(1)
            .tuesday(1)
            .wednesday(1)
            .thursday(1)
            .friday(1)
            .saturday(1)
            .build();

    public SelectedDays update(int day, boolean isSelected) {
        if (day < 0 || day > 7) {
            throw new RuntimeException("Could not set day at index: " + day + ". Must in the range [0,7).");
        }

        int selected = isSelected ? 1 : 0;

        switch (day) {
            case SUNDAY:    return this.withSunday(selected);
            case MONDAY:    return this.withMonday(selected);
            case TUESDAY:   return this.withTuesday(selected);
            case WEDNESDAY: return this.withWednesday(selected);
            case THURSDAY:  return this.withThursday(selected);
            case FRIDAY:    return this.withFriday(selected);
            case SATURDAY:  return this.withSaturday(selected);
        }

        return this;
    }

    public int[] toIntArray() {
        return new int[]{
                this.sunday,
                this.monday,
                this.tuesday,
                this.wednesday,
                this.thursday,
                this.friday,
                this.saturday,
        };
    }

    public boolean[] toBooleanArray() {
        return new boolean[]{
                this.sunday == 1,
                this.monday == 1,
                this.tuesday == 1,
                this.wednesday == 1,
                this.thursday == 1,
                this.friday == 1,
                this.saturday == 1,
        };
    }

    public boolean isAnyDaySelected() {
        return Arrays.stream(this.toIntArray()).anyMatch(i -> i == 1);
    }
}
