package com.jesseoberstein.alert.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jesseoberstein.alert.utils.IntentBuilder;

import javax.inject.Inject;

public class OnAlarmStop extends BroadcastReceiver {

    @Inject
    IntentBuilder intentBuilder;

    @Inject
    AlarmManager alarmManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        PendingIntent pendingIntent = intentBuilder.getNotificationsStartIntent(intent);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }
}
