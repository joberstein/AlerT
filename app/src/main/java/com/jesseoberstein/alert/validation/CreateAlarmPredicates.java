package com.jesseoberstein.alert.validation;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * Predicates for validating inputs when creating an alarm.
 */
public class CreateAlarmPredicates {
    private static final int NICKNAME_MAX_LEN = 12;

    /**
     * Is the nickname not empty and does it contain less than the max length of non-whitespace characters?
     * @return Is the nickname valid?
     */
    public static Predicate<String> isValidNickname() {
        return text -> {
            String trimmed = text.trim();
            return !trimmed.isEmpty() && (trimmed.length() <= NICKNAME_MAX_LEN);
        };
    }

    /**
     * Is the station one of the given stations?
     * @param stations A list of valid stations.
     * @return Is the station valid?
     */
    public static Predicate<String> isValidStation(String[] stations) {
        return text -> {
            String trimmed = text.trim().toLowerCase();
            return Arrays.stream(stations)
                    .filter(station -> station.toLowerCase().equals(trimmed))
                    .count() > 0;
        };
    }
}
