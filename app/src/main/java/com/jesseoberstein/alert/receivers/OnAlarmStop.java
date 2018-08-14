package com.jesseoberstein.alert.receivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jesseoberstein.alert.services.AlarmService;

import java.util.Optional;

public class OnAlarmStop extends BroadcastReceiver {
    private AlarmService alarmService;

    @Override
    public void onReceive(Context context, Intent intent) {
        alarmService = Optional.ofNullable(alarmService).orElseGet(() -> new AlarmService(context));
        PendingIntent pendingIntent = alarmService.getServiceStartIntent(intent);
        alarmService.getManager().cancel(pendingIntent);
        pendingIntent.cancel();
    }
}
