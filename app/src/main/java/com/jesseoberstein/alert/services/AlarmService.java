package com.jesseoberstein.alert.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.jesseoberstein.alert.models.RepeatType;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;
import com.jesseoberstein.alert.receivers.OnAlarmStart;
import com.jesseoberstein.alert.receivers.OnAlarmStop;

import java.util.Optional;

import static android.app.AlarmManager.INTERVAL_DAY;
import static android.app.AlarmManager.RTC;
import static android.app.AlarmManager.RTC_WAKEUP;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.app.PendingIntent.getBroadcast;
import static android.app.PendingIntent.getService;
import static com.jesseoberstein.alert.utils.AlarmUtils.buildAlarmUri;
import static com.jesseoberstein.alert.utils.AlarmUtils.getAlarmEndTime;
import static com.jesseoberstein.alert.utils.AlarmUtils.getAlarmStartTime;
import static com.jesseoberstein.alert.utils.Constants.ALARM;
import static com.jesseoberstein.alert.utils.Constants.ALARM_START_REQUEST_CODE;
import static com.jesseoberstein.alert.utils.Constants.ALARM_STOP_REQUEST_CODE;
import static com.jesseoberstein.alert.utils.Constants.SERVICE_START_REQUEST_CODE;

public class AlarmService {
    private Context context;
    private AlarmManager alarmManager;

    public AlarmService(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public AlarmManager getManager() {
        return this.alarmManager;
    }

    public void scheduleMbtaAlarm(UserAlarmWithRelations alarmWithRelations) {
        PendingIntent startIntent = getAlarmStartIntent(alarmWithRelations);
        PendingIntent stopIntent = getAlarmStopIntent(alarmWithRelations);
        UserAlarm alarm = alarmWithRelations.getAlarm();
        boolean isRepeating = !RepeatType.NEVER.equals(alarm.getRepeatType());

        cancelAlarm(startIntent, stopIntent);  // Ensure that this alarm isn't scheduled twice.
        scheduleAlarm(getAlarmStartTime(alarm), getAlarmEndTime(alarm), startIntent, stopIntent, isRepeating);
    }

    public void cancelMbtaAlarm(UserAlarmWithRelations alarmWithRelations) {
        PendingIntent startIntent = getAlarmStartIntent(alarmWithRelations);
        PendingIntent stopIntent = getAlarmStopIntent(alarmWithRelations);
        cancelAlarm(startIntent, stopIntent);
    }

    private void scheduleAlarm(long startTime, long endTime, PendingIntent startIntent, PendingIntent stopIntent, boolean isRepeating) {
        if (isRepeating) {
            this.getManager().setRepeating(RTC_WAKEUP, startTime, INTERVAL_DAY, startIntent);
            this.getManager().setInexactRepeating(RTC, endTime, INTERVAL_DAY, stopIntent);
        } else {
            this.getManager().setExact(RTC_WAKEUP, startTime, startIntent);
            this.getManager().set(RTC, endTime, stopIntent);
        }
    }

    private void cancelAlarm(PendingIntent startIntent, PendingIntent stopIntent) {
        this.getManager().cancel(startIntent);
        this.getManager().cancel(stopIntent);
    }

    private PendingIntent getAlarmStartIntent(UserAlarmWithRelations alarm) {
        Intent intent = new Intent(Intent.ACTION_INSERT, buildAlarmUri(alarm.getAlarm().getId()), context, OnAlarmStart.class);
        intent.putExtra(ALARM, alarm);
        return getBroadcast(context, ALARM_START_REQUEST_CODE, intent, FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getAlarmStopIntent(UserAlarmWithRelations alarm) {
        Intent intent = new Intent(Intent.ACTION_DELETE, buildAlarmUri(alarm.getAlarm().getId()), context, OnAlarmStop.class);
        intent.putExtra(ALARM, alarm);
        return getBroadcast(context, ALARM_STOP_REQUEST_CODE, intent, FLAG_UPDATE_CURRENT);
    }

    public PendingIntent getServiceStartIntent(Intent intent) {
        Intent serviceIntent = new Intent(Intent.ACTION_RUN, intent.getData(), context, MbtaRealtimeUpdatesService.class);
        Optional.ofNullable(intent.getExtras()).ifPresent(intent::putExtras);
        return getService(context, SERVICE_START_REQUEST_CODE, serviceIntent, FLAG_UPDATE_CURRENT);
    }
}
