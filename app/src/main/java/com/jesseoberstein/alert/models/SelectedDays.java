package com.jesseoberstein.alert.models;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.Arrays;

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

    public SelectedDays update(DayOfWeek day, boolean isSelected) {
        int selected = isSelected ? 1 : 0;

        switch (day) {
            case MONDAY:    return this.withMonday(selected);
            case TUESDAY:   return this.withTuesday(selected);
            case WEDNESDAY: return this.withWednesday(selected);
            case THURSDAY:  return this.withThursday(selected);
            case FRIDAY:    return this.withFriday(selected);
            case SATURDAY:  return this.withSaturday(selected);
            case SUNDAY:    return this.withSunday(selected);
        }

        return this;
    }

    public int[] toIntArray() {
        return new int[]{
                this.monday,
                this.tuesday,
                this.wednesday,
                this.thursday,
                this.friday,
                this.saturday,
                this.sunday,
        };
    }

    public boolean[] toBooleanArray() {
        return new boolean[]{
                this.monday == 1,
                this.tuesday == 1,
                this.wednesday == 1,
                this.thursday == 1,
                this.friday == 1,
                this.saturday == 1,
                this.sunday == 1,
        };
    }

    public boolean isAnyDaySelected() {
        return Arrays.stream(this.toIntArray()).anyMatch(i -> i == 1);
    }
}
