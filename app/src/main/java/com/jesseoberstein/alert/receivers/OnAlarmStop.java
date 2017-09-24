package com.jesseoberstein.alert.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OnAlarmStop extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO stop service for retrieving MBTA realtime arrivals and push notifications.
    }
}
