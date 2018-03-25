package com.jesseoberstein.alert.utils;

import android.content.Context;

import org.threeten.bp.Duration;

import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Calendar.getInstance;

public class DateTimeUtils {

    public static final String HOUR = "hour";
    public static final String MINUTE = "minute";
    private static final Pattern DURATION_HOUR_PATTERN = Pattern.compile("PT.*?(\\d+)H");
    private static final Pattern DURATION_MINS_PATTERN = Pattern.compile("PT.*?(\\d+)M");

    public static long unixTimestampToMillis(Long ts) {
        return ts * 1000;
    }

    public static boolean isUnixTimestampAfterCurrentTime(Long ts) {
        Calendar now = getInstance();
        now.setTimeInMillis(System.currentTimeMillis());
        Calendar arrival = getInstance();
        arrival.setTimeInMillis(unixTimestampToMillis(ts));
        return arrival.after(now);
    }

    public static String formatUnixTimestamp(Context context, Long ts) {
        return android.text.format.DateUtils.formatDateTime(context, unixTimestampToMillis(ts), android.text.format.DateUtils.FORMAT_SHOW_TIME);
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
     * Parse a given duration string for the given unit.
     * @param durationString Should be the string of a {@link Duration}, like "PT%dH%dM"
     * @param unit A singular unit, currently one of "hour", "minute"
     * @return The parsed duration as a formatted string (or an empty string if the unit isn't
     * supported or not in the duration string).
     */
    public static String parseDurationUnit(String durationString, String unit) {
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
}
