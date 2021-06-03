package com.jesseoberstein.alert.utils;

import com.jesseoberstein.alert.models.AlarmEndpoint;
import com.jesseoberstein.alert.models.SelectedDays;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;
import com.jesseoberstein.alert.models.mbta.Endpoint;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.stream.IntStream;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserAlarmSchedulerTest {

    @Mock
    private DateTimeHelper mockDateTimeHelper;

    @Mock
    private Calendar mockCalendar;

    private UserAlarm testAlarm;
    private UserAlarmScheduler userAlarmScheduler;
    private final int today = 0;
    private final int tomorrow = 1;
    private final int calendarToday = today + 1;
    private final int calendarTomorrow = tomorrow + 1;
    private final long NOW = 10L;

    @Before
    public void setup() {
        userAlarmScheduler = new UserAlarmScheduler(mockDateTimeHelper, mockCalendar);
        testAlarm = new UserAlarm();

        when(mockDateTimeHelper.getCurrentTime()).thenReturn(NOW);
        when(mockDateTimeHelper.getCurrentDay()).thenReturn(calendarToday);
        when(mockDateTimeHelper.getTomorrow()).thenReturn(calendarTomorrow);
    }

    private void offsetMockFiringTime(long millis) {
        when(mockDateTimeHelper.getTimeInMillis(anyInt(), anyInt())).thenReturn(NOW + millis);
    }

    @Test
    public void testSetDefaultAlarmTime() {
        userAlarmScheduler = new UserAlarmScheduler(mockDateTimeHelper, Calendar.getInstance());
        offsetMockFiringTime(1);

        userAlarmScheduler.setDefaultAlarmTime(testAlarm);
        verify(mockDateTimeHelper).getFormattedTime(NOW + 1);
    }

    @Test
    public void testRestoreAlarmTime_doesNothingForBlankTime() {
        userAlarmScheduler.restoreAlarmTime(testAlarm);
        assertNull(testAlarm.getTime());
    }

    @Test
    public void testRestoreAlarmTime_restoresExistingTime() {
        int hour = 1;
        int minutes = 2;

        testAlarm = testAlarm.toBuilder()
                .hour(hour)
                .minutes(minutes)
                .build();

        when(mockDateTimeHelper.getFormattedTime(anyLong())).thenReturn("1:02am");

        userAlarmScheduler.restoreAlarmTime(testAlarm);
        verify(mockDateTimeHelper).getTimeInMillis(hour, minutes);
        assertEquals("1:02am", testAlarm.getTime());
    }

    @Test
    public void testSetAlarmTime_doesNothingForBlankTime() {
        testAlarm = testAlarm.toBuilder()
                .hour(null)
                .minutes(null)
                .build();

        userAlarmScheduler.setAlarmTime(testAlarm);
        assertNull(testAlarm.getTime());
        assertNull(testAlarm.getNextFiringDayString());
    }

    @Test
    public void testSetAlarmTime_setsOnlyNewHour() {
        int hour = 1;
        int currentMinutes = 2;

        testAlarm = testAlarm.toBuilder()
                .hour(hour)
                .minutes(currentMinutes)
                .build();

        when(mockDateTimeHelper.getFormattedTime(any())).thenReturn("1:02am");

        userAlarmScheduler.setAlarmTime(testAlarm);
        verify(mockDateTimeHelper).getTimeInMillis(hour, currentMinutes);
        assertEquals("1:02am", testAlarm.getTime());
        assertEquals("Next alarm firing at 1:02am", testAlarm.getNextFiringDayString());
    }

    @Test
    public void testSetAlarmTime_setsOnlyNewMinutes() {
        int currentHour = 2;
        int minutes = 1;

        testAlarm = testAlarm.toBuilder()
                .hour(currentHour)
                .minutes(minutes)
                .build();

        when(mockDateTimeHelper.getFormattedTime(any())).thenReturn("2:01am");

        userAlarmScheduler.setAlarmTime(testAlarm);
        verify(mockDateTimeHelper).getTimeInMillis(currentHour, minutes);
        assertEquals("2:01am", testAlarm.getTime());
        assertEquals("Next alarm firing at 2:01am", testAlarm.getNextFiringDayString());
    }

    @Test
    public void testSetAlarmTime_setsNewTime() {
        int newHour = 1;
        int newMinutes = 1;

        testAlarm = testAlarm.toBuilder()
                .hour(newHour)
                .minutes(newMinutes)
                .build();

        when(mockDateTimeHelper.getFormattedTime(any())).thenReturn("1:01am");

        userAlarmScheduler.setAlarmTime(testAlarm);
        verify(mockDateTimeHelper).getTimeInMillis(newHour, newMinutes);
        assertEquals("1:01am", testAlarm.getTime());
        assertEquals("Next alarm firing at 1:01am", testAlarm.getNextFiringDayString());
    }

    @Test
    public void testIsTodaySelected() {
        SelectedDays selectedDays = SelectedDays.builder()
                .sunday(1)
                .monday(1)
                .wednesday(1)
                .friday(1)
                .saturday(1)
                .build();

        testAlarm = testAlarm.withSelectedDays(selectedDays);

        IntStream.range(0, 7).forEach(day -> {
            when(mockDateTimeHelper.getCurrentDay()).thenReturn(day);

            if (Arrays.asList(0,1,3,5,6).contains(day)) {
                assertTrue(userAlarmScheduler.isTodaySelected(testAlarm));
            } else {
                assertFalse(userAlarmScheduler.isTodaySelected(testAlarm));
            }
        });
    }

    @Test
    public void testSetNextFiringDayString_doesNothingForBlankDays() {
        userAlarmScheduler.setNextFiringDayString(testAlarm);
        assertNull(testAlarm.getNextFiringDayString());
    }

    @Test
    @Ignore("No way to select today yet")
    public void testSetNextFiringDayString_firesToday_beforeFiringTime() {
        offsetMockFiringTime(1);

        userAlarmScheduler.setNextFiringDayString(testAlarm);
        assertTrue(testAlarm.getNextFiringDayString().contains("Today"));
    }

    @Test
    @Ignore("No way to select today yet")
    public void testSetNextFiringDayString_firesToday_afterFiringTime() {
        offsetMockFiringTime(-1);

        userAlarmScheduler.setNextFiringDayString(testAlarm);
        assertTrue(testAlarm.getNextFiringDayString().contains("Next "));
    }

    @Ignore("No way to select tomorrow yet")
    public void testSetNextFiringDayString_doesNotFireToday() {
        offsetMockFiringTime(1);

        userAlarmScheduler.setNextFiringDayString(testAlarm);
        assertTrue(testAlarm.getNextFiringDayString().contains("Monday"));
    }

    @Test
    public void testNextGetFiringDay_noDaysSelected() {
        // Set the alarm time one minute in the future.
        offsetMockFiringTime(1);
        assertEquals(calendarToday, userAlarmScheduler.getNextFiringDay(testAlarm));

        // Set the alarm time one minute in the past.
        offsetMockFiringTime(-1);
        assertEquals(calendarTomorrow, userAlarmScheduler.getNextFiringDay(testAlarm));
    }

    @Ignore("No way to select today yet")
    public void testGetNextFiringDay_onlyTodaySelected() {
        // Set the alarm time one minute in the future.
        offsetMockFiringTime(1);
        assertEquals(calendarToday, userAlarmScheduler.getNextFiringDay(testAlarm));

        // Set the alarm time one minute in the past.
        offsetMockFiringTime(-1);
        assertEquals(calendarToday, userAlarmScheduler.getNextFiringDay(testAlarm));
    }

    @Ignore("No way to select future day yet")
    public void testGetNextFiringDay_todayNotSelected() {
        int selectedDay = today + 3;
        int calendarSelectedDay = selectedDay + 1;
//        selectDays(selectedDay);

        offsetMockFiringTime(1);
        assertEquals(calendarSelectedDay, userAlarmScheduler.getNextFiringDay(testAlarm));

        offsetMockFiringTime(-1);
        assertEquals(calendarSelectedDay, userAlarmScheduler.getNextFiringDay(testAlarm));
    }

    @Ignore("No way to select today or tomorrow yet")
    public void testGetNextFiringDay_todayAndTomorrowSelected() {
        offsetMockFiringTime(1);
        assertEquals(calendarToday, userAlarmScheduler.getNextFiringDay(testAlarm));

        offsetMockFiringTime(-1);
        assertEquals(calendarTomorrow, userAlarmScheduler.getNextFiringDay(testAlarm));
    }

    @Test
    public void createsAlarmEndpoints() {
        long alarmId = 1;
        long e1Id = 2;
        long e2Id = 3;
        Endpoint e1 = new Endpoint("Oak Grove", 0, "Orange");
        Endpoint e2 = new Endpoint("Forest Hills", 1, "Orange");

        e1.setId(e1Id);
        e2.setId(e2Id);

        testAlarm = testAlarm.withId(alarmId);

        UserAlarmWithRelations testAlarmWithRelations = UserAlarmWithRelations.builder()
                .alarm(testAlarm)
                .endpoints(Arrays.asList(e1, e2))
                .build();

        AlarmEndpoint[] expected = new AlarmEndpoint[]{
            new AlarmEndpoint(alarmId, e1Id),
            new AlarmEndpoint(alarmId, e2Id)
        };

        assertArrayEquals(expected, AlarmUtils.createAlarmEndpoints(testAlarmWithRelations));
    }
}