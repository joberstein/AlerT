package com.jesseoberstein.alert.model;

import com.jesseoberstein.alert.models.RepeatType;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserRoute;
import com.jesseoberstein.alert.models.UserStop;
import com.jesseoberstein.mbta.model.RouteType;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class UserAlarmTest {
    private UserAlarm testAlarm;

    @Before
    public void setup() {
        testAlarm = new UserAlarm();
    }

    @Test(expected = RuntimeException.class)
    public void throwsExceptionIfWeekdayArrayIsWrongLength() {
        testAlarm.setWeekdays(new int[]{});
    }

    @Test
    public void verifyGetsAndSetsDays() {
        int[] expected = {0, 0, 1, 0, 1, 1, 0};
        testAlarm.setWeekdays(expected);
        assertEquals(Arrays.toString(expected), Arrays.toString(testAlarm.getWeekdays()));
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
        testAlarm.setMonday(1);
        testAlarm.setWednesday(1);
        testAlarm.setThursday(1);
        testAlarm.setSaturday(1);
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
        testAlarm.setSunday(1);
        testAlarm.setThursday(1);
        verifyAlarmWeekdays(new int[]{1, 0, 0, 0, 1, 0, 0});

        testAlarm.setRepeatType(RepeatType.CUSTOM);
        assertEquals(1, testAlarm.getSunday());
        assertEquals(1, testAlarm.getThursday());
    }

    @Test
    public void verifyGetAndSetTime() {
        testAlarm.setTime(0, 0);
        assertEquals("12:00 am", testAlarm.getTime());

        testAlarm.setTime(5, 17);
        assertEquals("5:17 am", testAlarm.getTime());

        testAlarm.setTime(12, 30);
        assertEquals("12:30 pm", testAlarm.getTime());

        testAlarm.setTime(18, 51);
        assertEquals("6:51 pm", testAlarm.getTime());

        testAlarm.setTime(23, 4);
        assertEquals("11:04 pm", testAlarm.getTime());
    }

    @Test
    public void verifyStopIsResetWhenRouteChanged() {
        UserRoute testRoute = new UserRoute();
        testRoute.setRouteId("Orange");
        testRoute.setRouteName("Orange Line");
        testRoute.setRouteType(RouteType.SUBWAY);

        UserStop testStop = new UserStop();
        testStop.setStopId("place-forhl");
        testStop.setStopName("Forest Hills");

        testAlarm.setRoute(testRoute);
        testAlarm.setStop(testStop);
        assertEquals(testStop, testAlarm.getStop());

        testAlarm.setRoute(new UserRoute());
        assertNull(testAlarm.getStop());
    }

    private void verifyAlarmWeekdays(int[] expected) {
        assertEquals(Arrays.toString(expected), Arrays.toString(testAlarm.getWeekdays()));
    }
}
