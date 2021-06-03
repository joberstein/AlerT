package com.jesseoberstein.alert.model;

import com.jesseoberstein.alert.models.RepeatType;
import com.jesseoberstein.alert.models.SelectedDays;
import com.jesseoberstein.alert.models.UserAlarm;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

public class UserAlarmTest {
    private UserAlarm testAlarm;

    @Before
    public void setup() {
        testAlarm = UserAlarm.builder()
            .nickname("Test nickname")
            .routeId("routeId")
            .stopId("stopId")
            .directionId(2L)
            .build();
    }

    @Test
    public void testNoArgConstructor() {
        testAlarm = new UserAlarm();

        assertEquals(0, testAlarm.getId());
        assertEquals(RepeatType.NEVER, testAlarm.getRepeatType());
        assertEquals(SelectedDays.DEFAULT, testAlarm.getSelectedDays());
        assertEquals(30, testAlarm.getDuration());
        assertTrue(testAlarm.isActive());

        assertNull(testAlarm.getNickname());
        assertNull(testAlarm.getRouteId());
        assertNull(testAlarm.getStopId());
        assertNull(testAlarm.getDirectionId());
    }

    @Test
    public void verifyGetsAndSetsDays() {
        SelectedDays selectedDays = SelectedDays.builder()
                .tuesday(1)
                .thursday(1)
                .friday(1)
                .build();

        testAlarm.setSelectedDays(selectedDays);
        assertEquals(testAlarm.getSelectedDays(), selectedDays);
    }

    @Test
    public void verifyRepeatNeverSetsCorrectDays() {
        testAlarm.setRepeatType(RepeatType.NEVER);
        assertEquals(SelectedDays.DEFAULT, testAlarm.getSelectedDays());
    }

    @Test
    public void verifyRepeatWeekdaysSetsCorrectDays() {
        testAlarm.setRepeatType(RepeatType.WEEKDAYS);
        assertEquals(SelectedDays.WEEKDAYS, testAlarm.getSelectedDays());
    }

    @Test
    public void verifyRepeatWeekendsSetsCorrectDays() {
        testAlarm.setRepeatType(RepeatType.WEEKENDS);
        assertEquals(SelectedDays.WEEKENDS, testAlarm.getSelectedDays());
    }

    @Test
    public void verifyRepeatDailySetsCorrectDays() {
        testAlarm.setRepeatType(RepeatType.DAILY);
        assertEquals(SelectedDays.DAILY, testAlarm.getSelectedDays());
    }

    @Test
    public void verifyRepeatCustomSetsCorrectDays() {
        SelectedDays selectedDays = SelectedDays.builder()
                .monday(1)
                .wednesday(1)
                .thursday(1)
                .friday(1)
                .build();

        testAlarm.setRepeatType(RepeatType.CUSTOM);
        testAlarm.setSelectedDays(selectedDays);
        assertEquals(selectedDays, testAlarm.getSelectedDays());
    }

    @Test
    public void verifyWeekdaysSetWhenRepeatTypeIsChanged() {
        testAlarm.setRepeatType(RepeatType.DAILY);
        assertEquals(SelectedDays.DAILY, testAlarm.getSelectedDays());

        testAlarm.setRepeatType(RepeatType.CUSTOM);
        assertEquals(SelectedDays.DEFAULT, testAlarm.getSelectedDays());
    }

    @Test
    public void verifyCustomRepeatNotResetWhenReselected() {
        SelectedDays selectedDays = SelectedDays.builder()
                .sunday(1)
                .thursday(1)
                .build();

        testAlarm.setRepeatType(RepeatType.CUSTOM);
        testAlarm.setSelectedDays(selectedDays);
        assertEquals(selectedDays, testAlarm.getSelectedDays());

        testAlarm.setRepeatType(RepeatType.CUSTOM);
        assertEquals(selectedDays, testAlarm.getSelectedDays());
    }

    @Test
    public void verifyGetAndSetTime() {
        testAlarm.setTime("12:00 am");
        assertEquals("12:00 am", testAlarm.getTime());

        testAlarm.setTime("11:04 pm");
        assertEquals("11:04 pm", testAlarm.getTime());
    }

    @Test
    public void testValidAlarm() {
        assertTrue(testAlarm.isValid());
    }

    @Test
    public void testValidAlarmCustomRepeat() {
        // Once the repeat type is custom, days must be selected for the alarm to be valid.
        testAlarm.setRepeatType(RepeatType.CUSTOM);
        assertFalse(testAlarm.isValid());

        SelectedDays selectedDays = SelectedDays.builder().saturday(1).build();
        testAlarm.setSelectedDays(selectedDays);
        assertTrue(testAlarm.isValid());
    }

    @Test
    public void testValidAlarmRouteId() {
        testAlarm.setRouteId(null);
        assertFalse(testAlarm.isValid());

        testAlarm.setRouteId("");
        assertFalse(testAlarm.isValid());
    }

    @Test
    public void testValidAlarmStopId() {
        testAlarm.setStopId(null);
        assertFalse(testAlarm.isValid());

        testAlarm.setStopId("");
        assertFalse(testAlarm.isValid());
    }

    @Test
    public void testValidAlarmDirectionId() {
        testAlarm.setDirectionId(null);
        System.out.println(testAlarm);
        assertFalse(testAlarm.isValid());

        testAlarm.setDirectionId(-1L);
        assertFalse(testAlarm.isValid());
    }

    private void verifyAlarmWeekdays(int[] expected) {
        assertEquals(Arrays.toString(expected), Arrays.toString(testAlarm.getSelectedDays().toIntArray()));
    }
}
