package com.jesseoberstein.alert.utils;

import android.support.annotation.VisibleForTesting;

import org.threeten.bp.Duration;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import static com.jesseoberstein.alert.utils.Constants.HOUR;
import static com.jesseoberstein.alert.utils.Constants.MINUTE;

public class DateTimeHelper {
    private static final Pattern DURATION_HOUR_PATTERN = Pattern.compile("PT.*?(\\d+)H");
    private static final Pattern DURATION_MINS_PATTERN = Pattern.compile("PT.*?(\\d+)M");

    private final Calendar calendar;

    @Inject
    public DateTimeHelper(Calendar calendar) {
        this.calendar = calendar;
    }

    /**
     * Formats a duration into a human readable string; only supports hours and minutes currently.
     * @param duration The long represent the duration in minutes.
     * @return The duration formatted as a string of the following patterns:
     * - # hour(s)
     * - # minute(s)
     * - # hour(s) and # minute(s)
     */
    public static String getFormattedDuration(long duration) {
        String durationString = Duration.ofMinutes(duration).toString();
        String formattedHour = parseDurationUnit(durationString, HOUR);
        String formattedMins = parseDurationUnit(durationString, MINUTE);

        // Do nothing if both fields are empty.
        if (formattedHour.isEmpty() && formattedMins.isEmpty()) {
            return "";
        }

        // If both fields are set, insert an 'and' between them.
        if (!formattedHour.isEmpty() && !formattedMins.isEmpty()) {
            return formattedHour + " and " + formattedMins;
        }

        // At this point, only one of the fields was present, so just show that one.
        return !formattedMins.isEmpty() ? formattedMins : formattedHour;
    }

    /**
     * Get the formatted time from the given timestamp.
     * @param time A UNIX timestamp
     * @return Time string formatted as h:mm a
     */
    public String getFormattedTime(long time) {
        Date date = new Date();
        date.setTime(time);
        return new SimpleDateFormat("h:mm a", Locale.ENGLISH).format(date).toLowerCase();
    }

    /**
     * Parse a given duration string for the given unit.
     * @param durationString Should be the string of a {@link Duration}, like "PT%dH%dM"
     * @param unit A singular unit, currently one of "hour", "minute"
     * @return The parsed duration as a formatted string (or an empty string if the unit isn't
     * supported or not in the duration string).
     */
    @VisibleForTesting
    static String parseDurationUnit(String durationString, String unit) {
        Pattern pattern;

        switch (unit) {
            case HOUR:
                pattern = DURATION_HOUR_PATTERN;
                break;
            case MINUTE:
                pattern = DURATION_MINS_PATTERN;
                break;
            default:
                return "";
        }

        Matcher matcher = pattern.matcher(durationString);
        if (!matcher.find()) {
            return "";
        }

        long duration = Long.parseLong(matcher.group(1));
        return String.format(Locale.US, "%d %s%s", duration, unit, (duration == 1 ? "" : "s"));
    }

    /**
     * Gets the current day of a calendar instance.
     * @return One of the Calendar day constants.
     */
    int getCurrentDay() {
        calendar.setTime(new Date());
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    int getTomorrow() {
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    long getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
     * Gets the time using the given hour of day and minutes in milliseconds.
     * @param hour Set as the calendar's {@link Calendar#HOUR_OF_DAY}
     * @param minutes Set as the calendar's {@link Calendar#MINUTE}
     */
    long getTimeInMillis(int hour, int minutes) {
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);
        return calendar.getTimeInMillis();
    }
}
