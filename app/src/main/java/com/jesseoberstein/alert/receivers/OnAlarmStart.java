package com.jesseoberstein.alert.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.jesseoberstein.alert.utils.IntentBuilder;

import javax.inject.Inject;

import static android.app.AlarmManager.ELAPSED_REALTIME;
import static com.jesseoberstein.alert.utils.Constants.SECOND;

public class OnAlarmStart extends BaseReceiver {

    @Inject
    IntentBuilder intentBuilder;

    @Inject
    AlarmManager alarmManager;

    @Override
    public void onReceive(Context context, Intent alarmStartIntent) {
        super.onReceive(context, alarmStartIntent);

        PendingIntent serviceStartIntent = intentBuilder.getNotificationsStartIntent(alarmStartIntent);
        long triggerTime = SystemClock.elapsedRealtime() + 10 * SECOND;
        long interval = 60 * SECOND;
        alarmManager.setRepeating(ELAPSED_REALTIME, triggerTime, interval, serviceStartIntent);
    }
}
