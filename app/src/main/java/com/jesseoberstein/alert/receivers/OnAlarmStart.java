package com.jesseoberstein.alert.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jesseoberstein.alert.utils.AlarmUtils;
import com.jesseoberstein.alert.utils.Constants;

public class OnAlarmStart extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (AlarmUtils.shouldAlarmFireToday(intent.getIntArrayExtra(Constants.DAYS))) {
            String[] stopIds = intent.getStringArrayExtra(Constants.STOP_IDS);
            // TODO start service for retrieving MBTA realtime arrivals and push notifications.
        }
    }
}
