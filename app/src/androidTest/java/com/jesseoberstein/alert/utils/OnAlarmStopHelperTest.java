package com.jesseoberstein.alert.utils;

import android.content.Context;

import androidx.test.InstrumentationRegistry;

import com.jesseoberstein.alert.data.dao.UserAlarmDao;
import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.models.RepeatType;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Ignore("Going with a different implementation that uses server-side events, so tests will change.")
@RunWith(MockitoJUnitRunner.class)
public class OnAlarmStopHelperTest {

    private OnAlarmStopHelper onAlarmStopHelper;
    private UserAlarmWithRelations alarmWithRelations;

    @Mock
    private AppDatabase database;

    @Mock
    private UserAlarmScheduler userAlarmScheduler;

    @Mock
    private UserAlarm alarm;

    @Mock
    private UserAlarmDao userAlarmDao;

    @Before
    public void setup() {
        Context context = InstrumentationRegistry.getTargetContext();
        onAlarmStopHelper = new OnAlarmStopHelper(context, userAlarmScheduler, database);

        alarmWithRelations = new UserAlarmWithRelations();
        alarmWithRelations.setAlarm(alarm);

        when(alarm.getRepeatType()).thenReturn(RepeatType.NEVER);
        doNothing().when(alarm).setActive(false);
        doReturn(userAlarmDao).when(database).userAlarmDao();
        doReturn(1).when(userAlarmDao).update(new UserAlarm[]{alarm});
    }

    @Test
    public void testSingleFireAlarmDeactivated() {
        onAlarmStopHelper.onReceiveAlarms(Collections.singletonList(alarmWithRelations));
        verify(alarm, times(1)).setActive(false);
    }

    @Test
    public void testRepeatingAlarmNotDeactivated() {
        doReturn(RepeatType.CUSTOM).when(alarm).getRepeatType();
        onAlarmStopHelper.onReceiveAlarms(Collections.singletonList(alarmWithRelations));
        verify(alarm, never()).setActive(false);
    }
}
