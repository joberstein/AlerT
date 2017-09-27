package com.jesseoberstein.alert.receivers;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jesseoberstein.alert.utils.AlarmUtils;

public class OnAlarmStop extends BroadcastReceiver {
    private AlarmManager alarmManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (alarmManager == null) {
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }

        alarmManager.cancel(AlarmUtils.getAlarmUpdateIntent(context, intent));
    }
}
