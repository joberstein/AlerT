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
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Calendar;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AlarmUtilsTest {
    @Mock private Calendar mockCalendar;
    @Mock private UserAlarm testAlarm;
    @Mock private UserAlarmWithRelations testAlarmWithRelations;

    private int calendarToday;
    private int calendarTomorrow;
    private int today;
    private int tomorrow;

    @Before
    public void setup() {
        calendarToday = DateTimeUtils.getCurrentDay();
        calendarTomorrow = getFutureDayOnCalendar(calendarToday, 1);
        today = calendarToday - 1;
        tomorrow = getFutureDay(today, 1);
    }

    private void mockAlarmTime(int hour, int minutes) {
        when(testAlarm.getHour()).thenReturn(hour);
        when(testAlarm.getMinutes()).thenReturn(minutes);
    }

    private void mockAlarmDays(int[] days) {
        when(testAlarm.getSelectedDays()).thenReturn(new SelectedDays(days));
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
        int calendarSelectedDay = getFutureDayOnCalendar(calendarToday, 3);
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

        for (int day : days) {
            selectedDays[day] = 1;
        }

        mockAlarmDays(selectedDays);
    }

    private int getFutureDay(int day, int numDays) {
        return (day + numDays) % 7;
    }

    private int getFutureDayOnCalendar(int day, int numDays) {
        return getFutureDay(day - 1, numDays) + 1;
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