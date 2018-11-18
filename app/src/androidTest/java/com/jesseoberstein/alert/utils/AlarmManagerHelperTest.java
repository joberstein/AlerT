package com.jesseoberstein.alert.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;

import com.jesseoberstein.alert.models.RepeatType;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;
import com.jesseoberstein.alert.receivers.OnAlarmStart;
import com.jesseoberstein.alert.receivers.OnAlarmStop;
import com.jesseoberstein.alert.services.MbtaRealtimeUpdatesService;
import com.jesseoberstein.alert.tasks.QueryAlarmsTask;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static android.app.AlarmManager.INTERVAL_DAY;
import static android.app.AlarmManager.RTC;
import static android.app.AlarmManager.RTC_WAKEUP;
import static android.content.Intent.ACTION_DELETE;
import static android.content.Intent.ACTION_INSERT;
import static android.content.Intent.ACTION_RUN;
import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AlarmManagerHelperTest {

    private AlarmManagerHelper alarmManagerHelper;

    @Mock
    private AlarmManager alarmManager;

    @Mock
    private UserAlarmScheduler userAlarmScheduler;

    private UserAlarm alarm;
    private UserAlarmWithRelations alarmWithRelations;

    private PendingIntent alarmStartIntent;
    private PendingIntent alarmStopIntent;

    private final long startTime = 10000L;
    private final long endTime = 20000L;

    @Before
    public void setup() {
        IntentBuilder intentBuilder = new IntentBuilder(InstrumentationRegistry.getTargetContext());
        alarmManagerHelper = new AlarmManagerHelper(intentBuilder, alarmManager, userAlarmScheduler);

        alarm = new UserAlarm();
        alarm.setId(1);
        alarm.setRepeatType(RepeatType.DAILY);

        alarmWithRelations = new UserAlarmWithRelations();
        alarmWithRelations.setAlarm(alarm);
        alarmStartIntent = intentBuilder.getAlarmStartIntent(alarmWithRelations);
        alarmStopIntent = intentBuilder.getAlarmStopIntent(alarmWithRelations);

        when(userAlarmScheduler.getAlarmStartTime(alarm)).thenReturn(startTime);
        when(userAlarmScheduler.getAlarmEndTime(alarm)).thenReturn(endTime);
    }

    @Test
    public void testScheduleRepeatingUserAlarm() {
        alarmManagerHelper.scheduleUserAlarm(alarmWithRelations);
        verify(alarmManager).setRepeating(RTC_WAKEUP, startTime, INTERVAL_DAY, alarmStartIntent);
        verify(alarmManager).setInexactRepeating(RTC, endTime, INTERVAL_DAY, alarmStopIntent);
    }

    @Test
    public void testScheduleSingleUseUserAlarm() {
        alarm.setRepeatType(RepeatType.NEVER);
        alarmManagerHelper.scheduleUserAlarm(alarmWithRelations);
        verify(alarmManager).setExact(RTC_WAKEUP, startTime, alarmStartIntent);
        verify(alarmManager).set(RTC, endTime, alarmStopIntent);
    }

    @Test
    public void testCancelUserAlarm() {
        alarmManagerHelper.cancelUserAlarm(alarmWithRelations);
        verify(alarmManager).cancel(alarmStartIntent);
        verify(alarmManager).cancel(alarmStopIntent);
    }
}