package com.jesseoberstein.alert.utils;

import com.jesseoberstein.alert.models.UserAlarm;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Calendar;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AlarmUtilsTest {
    private Calendar mockCalendar;
    private UserAlarm testAlarm;
    private int calendarToday;
    private int calendarTomorrow;
    private int today;
    private int tomorrow;

    @Before
    public void setup() {
        mockCalendar = mock(Calendar.class);
        testAlarm = mock(UserAlarm.class);
        calendarToday = DateTimeUtils.getCurrentDay();
        calendarTomorrow = getFutureDay(calendarToday, 1);
        today = calendarToday - 1;
        tomorrow = getFutureDay(today, 1);
    }

    private void mockAlarmTime(int hour, int minutes) {
        when(testAlarm.getHour()).thenReturn(hour);
        when(testAlarm.getMinutes()).thenReturn(minutes);
    }

    private void mockAlarmDays(int[] days) {
        when(testAlarm.getWeekdays()).thenReturn(days);
    }

    @Test
    public void shouldAlarmFireToday() throws Exception {
        int[] smwfn = new int[]{-1,1,1,0,1,0,1,1};
        Mockito.doNothing().when(mockCalendar).setTimeInMillis(anyLong());
        assertIfAlarmShouldFireOnDay(smwfn, Calendar.SUNDAY, true);
        assertIfAlarmShouldFireOnDay(smwfn, Calendar.MONDAY, true);
        assertIfAlarmShouldFireOnDay(smwfn, Calendar.TUESDAY, false);
        assertIfAlarmShouldFireOnDay(smwfn, Calendar.WEDNESDAY, true);
        assertIfAlarmShouldFireOnDay(smwfn, Calendar.THURSDAY, false);
        assertIfAlarmShouldFireOnDay(smwfn, Calendar.FRIDAY, true);
        assertIfAlarmShouldFireOnDay(smwfn, Calendar.SATURDAY, true);;
    }

    @Test
    public void nextFiringDayWhenNoDaysSelected() {
        selectDays();

        // Set the alarm time one minute in the future.
        setAlarmTimeInNumMinutesFromNow(1);
        assertEquals(calendarToday, AlarmUtils.getNextFiringDay(testAlarm));

        // Set the alarm time one minute in the past.
        setAlarmTimeInNumMinutesFromNow(-1);
        int tomorrow = calendarTomorrow;
        assertEquals(tomorrow, AlarmUtils.getNextFiringDay(testAlarm));
    }

    @Test
    public void nextFiringDayWhenOnlyTodaySelected() {
        selectDays(today);

        // Set the alarm time one minute in the future.
        setAlarmTimeInNumMinutesFromNow(1);
        assertEquals(calendarToday, AlarmUtils.getNextFiringDay(testAlarm));

        // Set the alarm time one minute in the past.
        setAlarmTimeInNumMinutesFromNow(-1);
        assertEquals(calendarToday, AlarmUtils.getNextFiringDay(testAlarm));
    }

    @Test
    public void nextFiringDayWhenTodayNotSelected() {
        int selectedDay = getFutureDay(today, 3);
        int calendarSelectedDay = getFutureDay(calendarToday, 3);
        selectDays(selectedDay);

        // Set the alarm time one minute in the future.
        setAlarmTimeInNumMinutesFromNow(1);
        assertEquals(calendarSelectedDay, AlarmUtils.getNextFiringDay(testAlarm));

        // Set the alarm time one minute in the past.
        setAlarmTimeInNumMinutesFromNow(-1);
        assertEquals(calendarSelectedDay, AlarmUtils.getNextFiringDay(testAlarm));
    }

    @Test
    public void nextFiringDayWhenTodayAndTomorrowSelected() {
        selectDays(today, tomorrow);

        // Set the alarm time one minute in the future.
        setAlarmTimeInNumMinutesFromNow(1);
        assertEquals(calendarToday, AlarmUtils.getNextFiringDay(testAlarm));

        // Set the alarm time one minute in the past.
        setAlarmTimeInNumMinutesFromNow(-1);
        assertEquals(calendarTomorrow, AlarmUtils.getNextFiringDay(testAlarm));
    }

    private void selectDays(int... days) {
        int[] selectedDays = new int[7];
        Arrays.fill(selectedDays, 0);

        for (int day : days) {
            selectedDays[day] = 1;
        }

        mockAlarmDays(selectedDays);
    }

    private int getFutureDay(int day, int numDays) {
        return (day + numDays) % 7;
    }

    private void setAlarmTimeInNumMinutesFromNow(int numMinutes) {
        long now = DateTimeUtils.getCurrentTimeInMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now + (numMinutes * 1000L * 60));
        mockAlarmTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    private void assertIfAlarmShouldFireOnDay(int[] days, int day, boolean result) {
        when(mockCalendar.get(Calendar.DAY_OF_WEEK)).thenReturn(day);
        assertEquals(result, AlarmUtils.shouldAlarmFireToday(days, mockCalendar));
    }
}