package com.jesseoberstein.alert.models;

import android.arch.persistence.room.Ignore;
import android.support.annotation.VisibleForTesting;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.IntStream;

public class SelectedDays implements Serializable {
    private static final int SUNDAY = Calendar.SUNDAY - 1;
    private static final int MONDAY = Calendar.MONDAY - 1;
    private static final int TUESDAY = Calendar.TUESDAY - 1;
    private static final int WEDNESDAY = Calendar.WEDNESDAY - 1;
    private static final int THURSDAY = Calendar.THURSDAY - 1;
    private static final int FRIDAY = Calendar.FRIDAY - 1;
    private static final int SATURDAY = Calendar.SATURDAY - 1;
    private static final List<Integer> DAY_INDICES = Arrays.asList(SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY);

    private int monday;
    private int tuesday;
    private int wednesday;
    private int thursday;
    private int friday;
    private int saturday;
    private int sunday;

    @Ignore
    private int[] selectedDays = new int[7];

    @Ignore
    public SelectedDays() {
        Arrays.fill(this.selectedDays, 0);
    }

    @Ignore
    public SelectedDays(int[] selectedDays) {
        this.setAll(selectedDays);
    }

    // Constructor for Room
    public SelectedDays(int sunday, int monday, int tuesday, int wednesday, int thursday, int friday, int saturday) {
        setSunday(sunday);
        setMonday(monday);
        setTuesday(tuesday);
        setWednesday(wednesday);
        setThursday(thursday);
        setFriday(friday);
        setSaturday(saturday);
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

        int selected = isSelected ? 1 : 0;
        switch (day) {
            case SUNDAY:
                setSunday(selected);
                break;
            case MONDAY:
                setMonday(selected);
                break;
            case TUESDAY:
                setTuesday(selected);
                break;
            case WEDNESDAY:
                setWednesday(selected);
                break;
            case THURSDAY:
                setThursday(selected);
                break;
            case FRIDAY:
                setFriday(selected);
                break;
            case SATURDAY:
                setSaturday(selected);
        }
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
        IntStream.range(0, convertedDays.length).forEach(i -> convertedDays[i] = (this.selectedDays[i] == 1));
        return convertedDays;
    }

    public boolean isAnyDaySelected() {
        return Arrays.stream(this.selectedDays).anyMatch(i -> i == 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SelectedDays that = (SelectedDays) o;

        if (monday != that.monday) return false;
        if (tuesday != that.tuesday) return false;
        if (wednesday != that.wednesday) return false;
        if (thursday != that.thursday) return false;
        if (friday != that.friday) return false;
        if (saturday != that.saturday) return false;
        return sunday == that.sunday;
    }

    @Override
    public int hashCode() {
        int result = monday;
        result = 31 * result + tuesday;
        result = 31 * result + wednesday;
        result = 31 * result + thursday;
        result = 31 * result + friday;
        result = 31 * result + saturday;
        result = 31 * result + sunday;
        return result;
    }

    @Override
    public String toString() {
        return Arrays.toString(selectedDays);
    }
}
