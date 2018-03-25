package com.jesseoberstein.alert.models;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

public enum RepeatType {
    NEVER(new int[]{0, 0, 0, 0, 0, 0, 0}),
    WEEKDAYS(new int[]{0, 1, 1, 1, 1, 1, 0}),
    WEEKENDS(new int[]{1, 0, 0, 0, 0, 0, 1}),
    DAILY(new int[]{1, 1, 1, 1, 1, 1, 1}),
    CUSTOM(new int[]{0, 0, 0, 0, 0, 0, 0});

    private int[] selectedDays;

    RepeatType(int[] selectedDays) {
        this.selectedDays = selectedDays;
    }

    public int[] getSelectedDays() {
        return this.selectedDays;
    }

    public static String[] getRepeatTypes() {
        return Arrays.stream(RepeatType.values())
                .sorted(Comparator.comparing(Enum::ordinal))
                .map(RepeatType::toString)
                .collect(Collectors.toList())
                .toArray(new String[]{});
    }

    @Override
    public String toString() {
        switch (this) {
            case NEVER:
                return "Never";
            case WEEKDAYS:
                return "Mon - Fri";
            case WEEKENDS:
                return "Sat - Sun";
            case DAILY:
                return "Daily";
            case CUSTOM:
                return "Custom";
            default:
                return "Unknown";
        }
    }
}
