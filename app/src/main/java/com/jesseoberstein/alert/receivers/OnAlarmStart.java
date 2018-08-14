package com.jesseoberstein.alert.receivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.jesseoberstein.alert.models.UserAlarmWithRelations;
import com.jesseoberstein.alert.services.AlarmService;

import java.util.Optional;

import static android.app.AlarmManager.ELAPSED_REALTIME;
import static com.jesseoberstein.alert.utils.Constants.ALARM;

public class OnAlarmStart extends BroadcastReceiver {
    private AlarmService alarmService;
    private final int SECOND = 1000;

    @Override
    public void onReceive(Context context, Intent alarmStartIntent) {
        alarmService = Optional.ofNullable(alarmService).orElseGet(() -> new AlarmService(context));
        UserAlarmWithRelations alarm = (UserAlarmWithRelations) alarmStartIntent.getSerializableExtra(ALARM);

        if (alarm.getAlarm().shouldAlarmFireToday()) {
            PendingIntent serviceStartIntent = alarmService.getServiceStartIntent(alarmStartIntent);
            long triggerTime = SystemClock.elapsedRealtime() + 10 * SECOND;
            long interval = 60 * SECOND;
            this.alarmService.getManager().setRepeating(ELAPSED_REALTIME, triggerTime, interval, serviceStartIntent);
        }
    }
}
