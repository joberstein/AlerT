package com.jesseoberstein.alert.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.jesseoberstein.alert.models.UserAlarmWithRelations;
import com.jesseoberstein.alert.utils.IntentBuilder;
import com.jesseoberstein.alert.utils.UserAlarmScheduler;

import javax.inject.Inject;

import static android.app.AlarmManager.ELAPSED_REALTIME;
import static com.jesseoberstein.alert.utils.Constants.ALARM;
import static com.jesseoberstein.alert.utils.Constants.SECOND;

public class OnAlarmStart extends BroadcastReceiver {

    @Inject
    IntentBuilder intentBuilder;

    @Inject
    AlarmManager alarmManager;

    @Inject
    UserAlarmScheduler userAlarmScheduler;

    @Override
    public void onReceive(Context context, Intent alarmStartIntent) {
        UserAlarmWithRelations alarm = (UserAlarmWithRelations) alarmStartIntent.getSerializableExtra(ALARM);

        if (userAlarmScheduler.isTodaySelected(alarm.getAlarm())) {
            PendingIntent serviceStartIntent = intentBuilder.getNotificationsStartIntent(alarmStartIntent);
            long triggerTime = SystemClock.elapsedRealtime() + 10 * SECOND;
            long interval = 60 * SECOND;
            alarmManager.setRepeating(ELAPSED_REALTIME, triggerTime, interval, serviceStartIntent);
        }
    }
}
