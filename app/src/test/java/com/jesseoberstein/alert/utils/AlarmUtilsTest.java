package com.jesseoberstein.alert.utils;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Calendar;

import static org.mockito.Matchers.anyLong;

public class AlarmUtilsTest {
    private Calendar mockCalendar = Mockito.mock(Calendar.class);

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

    private void assertIfAlarmShouldFireOnDay(int[] days, int day, boolean result) {
        Mockito.when(mockCalendar.get(Calendar.DAY_OF_WEEK)).thenReturn(day);
        Assert.assertEquals(result, AlarmUtils.shouldAlarmFireToday(days, mockCalendar));
    }
}