package com.jesseoberstein.alert.utils;

import org.junit.Test;
import org.threeten.bp.Duration;

import java.util.HashMap;
import java.util.Map;

import static com.jesseoberstein.alert.utils.DateTimeUtils.HOUR;
import static com.jesseoberstein.alert.utils.DateTimeUtils.MINUTE;
import static com.jesseoberstein.alert.utils.DateTimeUtils.getFormattedDuration;
import static com.jesseoberstein.alert.utils.DateTimeUtils.parseDurationUnit;
import static junit.framework.Assert.assertEquals;

public class DateTimeUtilsTest {

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

    @Test
    public void verifyDurationFormatting() {
        formattedDurationMap.entrySet().parallelStream().forEach(entry -> {
            assertEquals(entry.getValue(), getFormattedDuration(entry.getKey()));
        });
    }

    @Test
    public void parsesHourFromDurationString() {
        parsedHourMap.entrySet().parallelStream().forEach(entry -> {
            assertEquals(entry.getValue(), parseDurationUnit(Duration.ofMinutes(entry.getKey()).toString(), HOUR));
        });
    }

    @Test
    public void parsesMinutesFromDurationString() {
        parsedMinutesMap.entrySet().parallelStream().forEach(entry -> {
            assertEquals(entry.getValue(), parseDurationUnit(Duration.ofMinutes(entry.getKey()).toString(), MINUTE));
        });
    }

    @Test
    public void testGetFormattedTime() {
        assertEquals("12:00 am", DateTimeUtils.getFormattedTime(0, 0));
        assertEquals("7:09 am", DateTimeUtils.getFormattedTime(7, 9));
        assertEquals("1:30 pm", DateTimeUtils.getFormattedTime(13, 30));
        assertEquals("7:00 pm", DateTimeUtils.getFormattedTime(18, 60));
        assertEquals("11:59 pm", DateTimeUtils.getFormattedTime(23, 59));
    }
}