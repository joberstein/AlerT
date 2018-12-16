package com.jesseoberstein.alert.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.VisibleForTesting;

import com.jesseoberstein.alert.receivers.OnAlarmStart;
import com.jesseoberstein.alert.receivers.OnAlarmStop;
import com.jesseoberstein.alert.services.MbtaRealtimeUpdatesService;

import java.util.Optional;

import javax.inject.Inject;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.app.PendingIntent.getBroadcast;
import static android.app.PendingIntent.getService;
import static com.jesseoberstein.alert.utils.AlarmUtils.buildAlarmUri;
import static com.jesseoberstein.alert.utils.Constants.ALARM_ID;
import static com.jesseoberstein.alert.utils.Constants.ALARM_START_REQUEST_CODE;
import static com.jesseoberstein.alert.utils.Constants.ALARM_STOP_REQUEST_CODE;
import static com.jesseoberstein.alert.utils.Constants.SERVICE_START_REQUEST_CODE;

public class IntentBuilder {
    private final Context context;

    @Inject
    public IntentBuilder(Context context) {
        this.context = context;
    }

    PendingIntent getAlarmStartIntent(long alarmId) {
        return buildBroadcastPendingIntent(ALARM_START_REQUEST_CODE, buildAlarmStartIntent(alarmId));
    }

    PendingIntent getAlarmStopIntent(long alarmId) {
        return buildBroadcastPendingIntent(ALARM_STOP_REQUEST_CODE, buildAlarmStopIntent(alarmId));
    }

    public PendingIntent getNotificationsStartIntent(Intent intent) {
        return buildServicePendingIntent(SERVICE_START_REQUEST_CODE, buildNotificationsStartIntent(intent));
    }

    @VisibleForTesting
    Intent buildAlarmStartIntent(long alarmId) {
        Intent intent = new Intent(Intent.ACTION_INSERT, buildAlarmUri(alarmId), context, OnAlarmStart.class);
        intent.putExtra(ALARM_ID, alarmId);
        return intent;
    }

    @VisibleForTesting
    Intent buildAlarmStopIntent(long alarmId) {
        Intent intent = new Intent(Intent.ACTION_DELETE, buildAlarmUri(alarmId), context, OnAlarmStop.class);
        intent.putExtra(ALARM_ID, alarmId);
        return intent;
    }

    @VisibleForTesting
    Intent buildNotificationsStartIntent(Intent intent) {
        Intent serviceIntent = new Intent(Intent.ACTION_RUN, intent.getData(), context, MbtaRealtimeUpdatesService.class);
        Optional.ofNullable(intent.getExtras()).ifPresent(serviceIntent::putExtras);
        return serviceIntent;
    }

    private PendingIntent buildBroadcastPendingIntent(int requestCode, Intent intent) {
        return getBroadcast(context, requestCode, intent, FLAG_UPDATE_CURRENT);
    }

    private PendingIntent buildServicePendingIntent(int requestCode, Intent intent) {
        return getService(context, requestCode, intent, FLAG_UPDATE_CURRENT);
    }
}
