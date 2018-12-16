package com.jesseoberstein.alert.utils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.threeten.bp.Duration;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.jesseoberstein.alert.utils.Constants.HOUR;
import static com.jesseoberstein.alert.utils.Constants.MINUTE;
import static com.jesseoberstein.alert.utils.DateTimeHelper.getFormattedDuration;
import static com.jesseoberstein.alert.utils.DateTimeHelper.parseDurationUnit;
import static junit.framework.Assert.assertEquals;

public class DateTimeHelperTest {

    @Mock
    private Calendar mockCalendar;

    private DateTimeHelper dateTimeHelper;

    private static final Map<Long, String> formattedDurationMap = new HashMap<>();
    static {
        formattedDurationMap.put(1L, "1 minute");
        formattedDurationMap.put(20L, "20 minutes");
        formattedDurationMap.put(60L, "1 hour");
        formattedDurationMap.put(61L, "1 hour and 1 minute");
        formattedDurationMap.put(120L, "2 hours");
        formattedDurationMap.put(121L, "2 hours and 1 minute");
        formattedDurationMap.put(147L, "2 hours and 27 minutes");
        formattedDurationMap.put(160L, "2 hours and 40 minutes");
    }

    private static final Map<Long, String> parsedHourMap = new HashMap<>();
    static {
        parsedHourMap.put(60L, "1 hour");
        parsedHourMap.put(120L, "2 hours");
        parsedHourMap.put(600L, "10 hours");
    }

    private static final Map<Long, String> parsedMinutesMap = new HashMap<>();
    static {
        parsedMinutesMap.put(1L, "1 minute");
        parsedMinutesMap.put(2L, "2 minutes");
        parsedMinutesMap.put(10L, "10 minutes");
        parsedMinutesMap.put(45L, "45 minutes");
        parsedMinutesMap.put(59L, "59 minutes");
    }

    private static final Map<String, int[]> formattedTimeMap = new HashMap<>();
    static {
        formattedTimeMap.put("12:00 am", new int[]{0, 0});
        formattedTimeMap.put("7:09 am", new int[]{7, 9});
        formattedTimeMap.put("1:30 pm", new int[]{13, 30});
        formattedTimeMap.put("7:00 pm", new int[]{18, 60});
        formattedTimeMap.put("11:59 pm", new int[]{23, 59});
    }

    @Before
    public void setup() {
        dateTimeHelper = new DateTimeHelper(mockCalendar);
    }

    @Test
    public void testGetFormattedDuration() {
        formattedDurationMap.entrySet().parallelStream().forEach(entry -> {
            assertEquals(entry.getValue(), getFormattedDuration(entry.getKey()));
        });
    }

    @Test
    public void testParseDurationUnit_hour() {
        parsedHourMap.entrySet().parallelStream().forEach(entry -> {
            assertEquals(entry.getValue(), parseDurationUnit(Duration.ofMinutes(entry.getKey()).toString(), HOUR));
        });
    }

    @Test
    public void testParseDurationUnit_minutes() {
        parsedMinutesMap.entrySet().parallelStream().forEach(entry -> {
            assertEquals(entry.getValue(), parseDurationUnit(Duration.ofMinutes(entry.getKey()).toString(), MINUTE));
        });
    }

    @Test
    public void testGetFormattedTime() {
        formattedTimeMap.entrySet().parallelStream().forEach(entry -> {
            int hour = entry.getValue()[0];
            int minutes = entry.getValue()[1];
            assertEquals(entry.getKey(), dateTimeHelper.getFormattedTime(calculateMillis(hour, minutes)));
        });
    }

    private long calculateMillis(int hour, int minute) {
        long offsetToMidnight = 60 * 60 * 1000 * 5;
        return offsetToMidnight + (60 * 60 * 1000 * hour) + (60 * 1000 * minute);
    }
}