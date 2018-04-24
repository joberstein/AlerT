package com.jesseoberstein.alert.models;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SelectedDays implements Serializable {
    private static final int SUNDAY = Calendar.SUNDAY - 1;
    private static final int MONDAY = Calendar.MONDAY - 1;
    private static final int TUESDAY = Calendar.TUESDAY - 1;
    private static final int WEDNESDAY = Calendar.WEDNESDAY - 1;
    private static final int THURSDAY = Calendar.THURSDAY - 1;
    private static final int FRIDAY = Calendar.FRIDAY - 1;
    private static final int SATURDAY = Calendar.SATURDAY - 1;
    private static final List<Integer> DAY_INDICES = Arrays.asList(SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY);

    private int[] selectedDays = new int[7];

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

    public SelectedDays() {
        Arrays.fill(this.selectedDays, 0);
    }

    public SelectedDays(int[] selectedDays) {
        this.setAll(selectedDays);
    }

    public int getMonday() {
        return monday;
    }

    public void setMonday(int monday) {
        this.monday = monday;
        this.selectedDays[MONDAY] = monday;
    }

    public int getTuesday() {
        return tuesday;
    }

    public void setTuesday(int tuesday) {
        this.tuesday = tuesday;
        this.selectedDays[TUESDAY] = tuesday;
    }

    public int getWednesday() {
        return wednesday;
    }

    public void setWednesday(int wednesday) {
        this.wednesday = wednesday;
        this.selectedDays[WEDNESDAY] = wednesday;
    }

    public int getThursday() {
        return thursday;
    }

    public void setThursday(int thursday) {
        this.thursday = thursday;
        this.selectedDays[THURSDAY] = thursday;
    }

    public int getFriday() {
        return friday;
    }

    public void setFriday(int friday) {
        this.friday = friday;
        this.selectedDays[FRIDAY] = friday;
    }

    public int getSaturday() {
        return saturday;
    }

    public void setSaturday(int saturday) {
        this.saturday = saturday;
        this.selectedDays[SATURDAY] = saturday;
    }

    public int getSunday() {
        return sunday;
    }

    public void setSunday(int sunday) {
        this.sunday = sunday;
        this.selectedDays[SUNDAY] = sunday;
    }

    public void setDay(int day, boolean isSelected) {
        if (day < 0 || day > 7) {
            throw new RuntimeException("Could not set day at index: " + day + ". Must in the range [0,7).");
        }
        this.selectedDays[day] = isSelected ? 1 : 0;
    }

    private void setAll(int[] selectedDays) {
        if (selectedDays.length != 7) {
            String msg = "Could not set selected days: given array has " + selectedDays.length + " elements; should have exactly 7.";
            throw new RuntimeException(msg);
        }

        DAY_INDICES.forEach(i -> setDay(i, selectedDays[i] == 1));
    }

    public int[] toIntArray() {
        return this.selectedDays;
    }

    public boolean[] toBooleanArray() {
        boolean[] convertedDays = new boolean[this.selectedDays.length];
        Arrays.stream(this.selectedDays).forEach(day -> convertedDays[day] = (day == 1));
        return convertedDays;
    }

    @Override
    public String toString() {
        return Arrays.toString(selectedDays);
    }
}
