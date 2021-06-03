package com.jesseoberstein.alert.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jesseoberstein.alert.utils.IntentBuilder;
import com.jesseoberstein.alert.utils.OnAlarmStopHelper;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import static com.jesseoberstein.alert.utils.Constants.ALARM_ID;

@AndroidEntryPoint
public class OnAlarmStop extends BroadcastReceiver {

    @Inject
    IntentBuilder intentBuilder;

    @Inject
    AlarmManager alarmManager;

    @Inject
    OnAlarmStopHelper onAlarmStopHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        long alarmId = intent.getLongExtra(ALARM_ID, -1);
        onAlarmStopHelper.deactivateSingleFireAlarm(alarmId);

        PendingIntent pendingIntent = intentBuilder.getNotificationsStartIntent(intent);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }
}
