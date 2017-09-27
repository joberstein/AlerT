package com.jesseoberstein.alert.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.jesseoberstein.alert.utils.AlarmUtils;

import static com.jesseoberstein.alert.utils.Constants.DAYS;

public class OnAlarmStart extends BroadcastReceiver {
    private AlarmManager alarmManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (alarmManager == null) {
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }

        if (AlarmUtils.shouldAlarmFireToday(intent.getIntArrayExtra(DAYS))) {
            PendingIntent pendingIntent = AlarmUtils.getAlarmUpdateIntent(context, intent);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), 60*1000, pendingIntent);
        }
    }
}
