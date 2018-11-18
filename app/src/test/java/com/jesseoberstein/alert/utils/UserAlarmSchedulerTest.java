package com.jesseoberstein.alert.utils;

import com.jesseoberstein.alert.models.AlarmEndpoint;
import com.jesseoberstein.alert.models.SelectedDays;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;
import com.jesseoberstein.alert.models.mbta.Endpoint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserAlarmSchedulerTest {

    @Mock private Calendar mockCalendar;
    @Mock private DateTimeHelper mockDateTimeHelper;
    @Mock private UserAlarm testAlarm;
    @Mock private UserAlarmWithRelations testAlarmWithRelations;

    private UserAlarmScheduler userAlarmScheduler;
    private final int today = 0;
    private final int tomorrow = 1;
    private final int calendarToday = today + 1;
    private final int calendarTomorrow = tomorrow + 1;
    private final long NOW = 10L;

    @Before
    public void setup() {
        userAlarmScheduler = new UserAlarmScheduler(mockDateTimeHelper, mockCalendar);

        when(mockDateTimeHelper.getCurrentTime()).thenReturn(NOW);
        when(mockDateTimeHelper.getCurrentDay()).thenReturn(calendarToday);
        when(mockDateTimeHelper.getTomorrow()).thenReturn(calendarTomorrow);
    }

    private void offsetMockFiringTime(long millis) {
        when(mockDateTimeHelper.getTimeInMillis(anyInt(), anyInt())).thenReturn(NOW + millis);
    }

    private void mockAlarmDays(int[] days) {
        when(testAlarm.getSelectedDays()).thenReturn(new SelectedDays(days));
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
        verify(testAlarm, never()).setTime(anyString());
    }

    @Test
    public void testRestoreAlarmTime_restoresExistingTime() {
        int hour = 1;
        int minutes = 2;
        when(testAlarm.getHour()).thenReturn(hour);
        when(testAlarm.getMinutes()).thenReturn(minutes);

        userAlarmScheduler.restoreAlarmTime(testAlarm);
        verify(mockDateTimeHelper).getTimeInMillis(hour, minutes);
        verify(testAlarm).setTime(any());
    }

    @Test
    public void testSetAlarmTime_doesNothingForBlankTime() {
        userAlarmScheduler.setAlarmTime(testAlarm, null, null);
        verify(testAlarm, never()).setTime(any());
        verify(testAlarm, never()).setNextFiringDayString(any());
    }

    @Test
    public void testSetAlarmTime_setsOnlyNewHour() {
        int currentMinutes = 2;
        when(testAlarm.getHour()).thenReturn(currentMinutes);
        when(testAlarm.getMinutes()).thenReturn(currentMinutes);
        selectDays();

        userAlarmScheduler.setAlarmTime(testAlarm, 1, currentMinutes);
        verify(mockDateTimeHelper).getTimeInMillis(1, currentMinutes);
        verify(testAlarm).setTime(any());
        verify(testAlarm).setNextFiringDayString(anyString());
    }

    @Test
    public void testSetAlarmTime_setsOnlyNewMinutes() {
        int currentHour = 2;
        when(testAlarm.getHour()).thenReturn(currentHour);
        when(testAlarm.getMinutes()).thenReturn(currentHour);
        selectDays();

        userAlarmScheduler.setAlarmTime(testAlarm, currentHour, 1);
        verify(mockDateTimeHelper).getTimeInMillis(currentHour, 1);
        verify(testAlarm).setTime(any());
        verify(testAlarm).setNextFiringDayString(anyString());
    }

    @Test
    public void testSetAlarmTime_setsNewTime() {
        int newHour = 1;
        int newMinutes = 1;
        when(testAlarm.getHour()).thenReturn(2);
        when(testAlarm.getMinutes()).thenReturn(2);
        selectDays();

        userAlarmScheduler.setAlarmTime(testAlarm, newHour, newMinutes);
        verify(mockDateTimeHelper).getTimeInMillis(newHour, newMinutes);
        verify(testAlarm).setTime(any());
        verify(testAlarm).setNextFiringDayString(anyString());
    }

    @Test
    public void testIsTodaySelected() throws Exception {
        int[] smwfn = new int[]{1,1,0,1,0,1,1};
        HashMap<Integer, Boolean> daysMap = new HashMap();
        daysMap.put(Calendar.SUNDAY, true);
        daysMap.put(Calendar.MONDAY, true);
        daysMap.put(Calendar.TUESDAY, false);
        daysMap.put(Calendar.WEDNESDAY, true);
        daysMap.put(Calendar.THURSDAY, false);
        daysMap.put(Calendar.FRIDAY, true);
        daysMap.put(Calendar.SATURDAY, true);
        daysMap.forEach((key, value) -> assertIfAlarmShouldFireOnDay(smwfn, key, value));
    }

    @Test
    public void testSetNextFiringDayString_doesNothingForBlankDays() {
        userAlarmScheduler.setNextFiringDayString(testAlarm);
        verify(testAlarm, never()).setNextFiringDayString(any());
    }

    @Test
    public void testSetNextFiringDayString_firesToday_beforeFiringTime() {
        selectDays(today);
        offsetMockFiringTime(1);

        userAlarmScheduler.setNextFiringDayString(testAlarm);
        verify(testAlarm).setNextFiringDayString(contains("Today"));
    }

    @Test
    public void testSetNextFiringDayString_firesToday_afterFiringTime() {
        selectDays(today);
        offsetMockFiringTime(-1);

        userAlarmScheduler.setNextFiringDayString(testAlarm);
        verify(testAlarm).setNextFiringDayString(contains("Next "));
    }

    @Test
    public void testSetNextFiringDayString_doesNotFireToday() {
        selectDays(tomorrow);
        offsetMockFiringTime(1);

        userAlarmScheduler.setNextFiringDayString(testAlarm);
        verify(testAlarm).setNextFiringDayString(contains("Monday"));
    }

    @Test
    public void testNextGetFiringDay_noDaysSelected() {
        selectDays();

        // Set the alarm time one minute in the future.
        offsetMockFiringTime(1);
        assertEquals(calendarToday, userAlarmScheduler.getNextFiringDay(testAlarm));

        // Set the alarm time one minute in the past.
        offsetMockFiringTime(-1);
        assertEquals(calendarTomorrow, userAlarmScheduler.getNextFiringDay(testAlarm));
    }

    @Test
    public void testGetNextFiringDay_onlyTodaySelected() {
        selectDays(today);

        // Set the alarm time one minute in the future.
        offsetMockFiringTime(1);
        assertEquals(calendarToday, userAlarmScheduler.getNextFiringDay(testAlarm));

        // Set the alarm time one minute in the past.
        offsetMockFiringTime(-1);
        assertEquals(calendarToday, userAlarmScheduler.getNextFiringDay(testAlarm));
    }

    @Test
    public void testGetNextFiringDay_todayNotSelected() {
        int selectedDay = today + 3;
        int calendarSelectedDay = selectedDay + 1;
        selectDays(selectedDay);

        offsetMockFiringTime(1);
        assertEquals(calendarSelectedDay, userAlarmScheduler.getNextFiringDay(testAlarm));

        offsetMockFiringTime(-1);
        assertEquals(calendarSelectedDay, userAlarmScheduler.getNextFiringDay(testAlarm));
    }

    @Test
    public void testGetNextFiringDay_todayAndTomorrowSelected() {
        selectDays(today, tomorrow);

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

        when(testAlarm.getId()).thenReturn(alarmId);
        when(testAlarmWithRelations.getAlarm()).thenReturn(testAlarm);
        when(testAlarmWithRelations.getEndpoints()).thenReturn(Arrays.asList(e1, e2));

        AlarmEndpoint[] expected = new AlarmEndpoint[]{
            new AlarmEndpoint(alarmId, e1Id),
            new AlarmEndpoint(alarmId, e2Id)
        };

        assertTrue(Arrays.equals(expected, AlarmUtils.createAlarmEndpoints(testAlarmWithRelations)));
    }

    private void selectDays(int... days) {
        int[] selectedDays = new int[7];
        Arrays.fill(selectedDays, 0);
        Arrays.stream(days).forEach(day -> selectedDays[day] = 1);
        mockAlarmDays(selectedDays);
    }

    private void assertIfAlarmShouldFireOnDay(int[] days, int day, boolean result) {
        when(mockDateTimeHelper.getCurrentDay()).thenReturn(day);
        mockAlarmDays(days);
        assertEquals(result, userAlarmScheduler.isTodaySelected(testAlarm));
    }
}