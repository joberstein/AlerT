package com.jesseoberstein.alert.utils;

import android.content.Intent;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;

import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;
import com.jesseoberstein.alert.receivers.OnAlarmStart;
import com.jesseoberstein.alert.receivers.OnAlarmStop;
import com.jesseoberstein.alert.services.MbtaRealtimeUpdatesService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static android.content.Intent.ACTION_DELETE;
import static android.content.Intent.ACTION_INSERT;
import static android.content.Intent.ACTION_RUN;
import static com.jesseoberstein.alert.utils.Constants.ALARM;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IntentBuilderTest {

    private IntentBuilder intentBuilder;

    private UserAlarmWithRelations alarmWithRelations;

    @Mock
    private UserAlarm alarm;

    @Before
    public void setup() {
        intentBuilder = new IntentBuilder(InstrumentationRegistry.getTargetContext());

        when(alarm.getId()).thenReturn(1L);
        alarmWithRelations = new UserAlarmWithRelations();
        alarmWithRelations.setAlarm(alarm);
    }

    @Test
    public void testBuildAlarmStartIntent() {
        Intent intent = intentBuilder.buildAlarmStartIntent(alarmWithRelations);
        assertEquals(ACTION_INSERT, intent.getAction());
        assertEquals(AlarmUtils.buildAlarmUri(1L), intent.getData());
        assertEquals(OnAlarmStart.class.getName(), intent.getComponent().getClassName());
        assertEquals(alarmWithRelations, intent.getExtras().getSerializable(ALARM));
    }

    @Test
    public void testBuildAlarmStopIntent() {
        Intent intent = intentBuilder.buildAlarmStopIntent(alarmWithRelations);
        assertEquals(ACTION_DELETE, intent.getAction());
        assertEquals(AlarmUtils.buildAlarmUri(1L), intent.getData());
        assertEquals(OnAlarmStop.class.getName(), intent.getComponent().getClassName());
        assertEquals(alarmWithRelations, intent.getExtras().getSerializable(ALARM));
    }

    @Test
    public void testBuildNotificationsStartIntent() {
        Intent intent = new Intent();
        Uri uri = new Uri.Builder().scheme("data").path("somewhere").build();
        intent.setData(uri);
        intent.putExtra("extra", "test");

        Intent notificationsIntent = intentBuilder.buildNotificationsStartIntent(intent);
        assertEquals(ACTION_RUN, notificationsIntent.getAction());
        assertEquals(uri, notificationsIntent.getData());
        assertEquals(MbtaRealtimeUpdatesService.class.getName(), notificationsIntent.getComponent().getClassName());
        assertEquals("test", notificationsIntent.getExtras().getString("extra"));
    }
}