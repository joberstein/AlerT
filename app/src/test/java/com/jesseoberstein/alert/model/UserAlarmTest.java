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
        testAlarm = new UserAlarm();
        testAlarm.setNickname("Test nickname");
        testAlarm.setRouteId("routeId");
        testAlarm.setStopId("stopId");
        testAlarm.setDirectionId(2);
    }

    @Test
    public void testNoArgConstructor() {
        testAlarm = new UserAlarm();

        assertEquals(0, testAlarm.getId());
        assertEquals(RepeatType.NEVER, testAlarm.getRepeatType());
        assertEquals(new SelectedDays(), testAlarm.getSelectedDays());
        assertEquals(30, testAlarm.getDuration());
        assertEquals(true, testAlarm.isActive());
        assertNull(testAlarm.getNickname());
        assertNull(testAlarm.getRouteId());
        assertNull(testAlarm.getStopId());
        assertEquals(-1, testAlarm.getDirectionId());
    }

    @Test
    public void testSingleArgConstructor() {
        testAlarm.setTime("10:30 am");
        testAlarm.setDuration(30);
        testAlarm.setRepeatType(RepeatType.CUSTOM);
        testAlarm.setSelectedDays(new int[]{1,0,1,0,1,0,1});
        testAlarm.setActive(false);

        assertEquals(testAlarm, new UserAlarm(testAlarm));
    }

    @Test
    public void verifyGetsAndSetsDays() {
        int[] expected = {0, 0, 1, 0, 1, 1, 0};
        testAlarm.setSelectedDays(expected);
        assertTrue(Arrays.equals(expected, testAlarm.getSelectedDays().toIntArray()));
    }

    @Test
    public void verifyRepeatNeverSetsCorrectDays() {
        testAlarm.setRepeatType(RepeatType.NEVER);
        verifyAlarmWeekdays(new int[]{0, 0, 0, 0, 0, 0, 0});
    }

    @Test
    public void verifyRepeatWeekdaysSetsCorrectDays() {
        testAlarm.setRepeatType(RepeatType.WEEKDAYS);
        verifyAlarmWeekdays(new int[]{0, 1, 1, 1, 1, 1, 0});
    }

    @Test
    public void verifyRepeatWeekendsSetsCorrectDays() {
        testAlarm.setRepeatType(RepeatType.WEEKENDS);
        verifyAlarmWeekdays(new int[]{1, 0, 0, 0, 0, 0, 1});
    }

    @Test
    public void verifyRepeatDailySetsCorrectDays() {
        testAlarm.setRepeatType(RepeatType.DAILY);
        verifyAlarmWeekdays(new int[]{1, 1, 1, 1, 1, 1, 1});
    }

    @Test
    public void verifyRepeatCustomSetsCorrectDays() {
        testAlarm.setRepeatType(RepeatType.CUSTOM);
        testAlarm.getSelectedDays().setMonday(1);
        testAlarm.getSelectedDays().setWednesday(1);
        testAlarm.getSelectedDays().setThursday(1);
        testAlarm.getSelectedDays().setSaturday(1);
        verifyAlarmWeekdays(new int[]{0, 1, 0, 1, 1, 0, 1});
    }

    @Test
    public void verifyWeekdaysSetWhenRepeatTypeIsChanged() {
        int[] expected = new int[7];

        testAlarm.setRepeatType(RepeatType.DAILY);
        Arrays.fill(expected, 1);
        verifyAlarmWeekdays(expected);

        testAlarm.setRepeatType(RepeatType.CUSTOM);
        Arrays.fill(expected, 0);
        verifyAlarmWeekdays(expected);
    }

    @Test
    public void verifyCustomRepeatNotResetWhenReselected() {
        testAlarm.setRepeatType(RepeatType.CUSTOM);
        testAlarm.getSelectedDays().setSunday(1);
        testAlarm.getSelectedDays().setThursday(1);
        verifyAlarmWeekdays(new int[]{1, 0, 0, 0, 1, 0, 0});

        testAlarm.setRepeatType(RepeatType.CUSTOM);
        assertEquals(1, testAlarm.getSelectedDays().getSunday());
        assertEquals(1, testAlarm.getSelectedDays().getThursday());
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

        testAlarm.setSelectedDays(new int[]{0, 0, 0, 0, 0, 0, 1});
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
        testAlarm.setDirectionId(-1);
        assertFalse(testAlarm.isValid());
    }

    private void verifyAlarmWeekdays(int[] expected) {
        assertEquals(Arrays.toString(expected), Arrays.toString(testAlarm.getSelectedDays().toIntArray()));
    }
}
