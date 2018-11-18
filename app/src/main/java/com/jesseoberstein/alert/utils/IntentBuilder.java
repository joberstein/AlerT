package com.jesseoberstein.alert.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.VisibleForTesting;

import com.jesseoberstein.alert.models.UserAlarmWithRelations;
import com.jesseoberstein.alert.receivers.OnAlarmStart;
import com.jesseoberstein.alert.receivers.OnAlarmStop;
import com.jesseoberstein.alert.services.MbtaRealtimeUpdatesService;

import java.util.Optional;

import javax.inject.Inject;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.app.PendingIntent.getBroadcast;
import static android.app.PendingIntent.getService;
import static com.jesseoberstein.alert.utils.AlarmUtils.buildAlarmUri;
import static com.jesseoberstein.alert.utils.Constants.ALARM;
import static com.jesseoberstein.alert.utils.Constants.ALARM_START_REQUEST_CODE;
import static com.jesseoberstein.alert.utils.Constants.ALARM_STOP_REQUEST_CODE;
import static com.jesseoberstein.alert.utils.Constants.SERVICE_START_REQUEST_CODE;

public class IntentBuilder {
    private final Context context;

    @Inject
    public IntentBuilder(Context context) {
        this.context = context;
    }

    PendingIntent getAlarmStartIntent(UserAlarmWithRelations alarm) {
        return buildBroadcastPendingIntent(ALARM_START_REQUEST_CODE, buildAlarmStartIntent(alarm));
    }

    PendingIntent getAlarmStopIntent(UserAlarmWithRelations alarm) {
        return buildBroadcastPendingIntent(ALARM_STOP_REQUEST_CODE, buildAlarmStopIntent(alarm));
    }

    public PendingIntent getNotificationsStartIntent(Intent intent) {
        return buildServicePendingIntent(SERVICE_START_REQUEST_CODE, buildNotificationsStartIntent(intent));
    }

    @VisibleForTesting
    public Intent buildAlarmStartIntent(UserAlarmWithRelations alarm) {
        Intent intent = new Intent(Intent.ACTION_INSERT, buildAlarmUri(alarm.getAlarm().getId()), context, OnAlarmStart.class);
        intent.putExtra(ALARM, alarm);
        return intent;
    }

    @VisibleForTesting
    public Intent buildAlarmStopIntent(UserAlarmWithRelations alarm) {
        Intent intent = new Intent(Intent.ACTION_DELETE, buildAlarmUri(alarm.getAlarm().getId()), context, OnAlarmStop.class);
        intent.putExtra(ALARM, alarm);
        return intent;
    }

    @VisibleForTesting
    public Intent buildNotificationsStartIntent(Intent intent) {
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
