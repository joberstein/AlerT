package com.jesseoberstein.alert.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.activities.alarms.ViewAlarms;
import com.jesseoberstein.alert.models.UserAlarm;

import java.util.Calendar;
import java.util.Objects;
import java.util.stream.IntStream;

import static android.app.AlarmManager.INTERVAL_FIFTEEN_MINUTES;
import static android.app.AlarmManager.RTC;
import static android.app.AlarmManager.RTC_WAKEUP;
import static com.jesseoberstein.alert.utils.Constants.ALARM_START_REQUEST_CODE;
import static com.jesseoberstein.alert.utils.Constants.ALARM_STOP_REQUEST_CODE;

public class AlarmUtils {
    private static Calendar calendar = Calendar.getInstance();

    public static void scheduleOrCancelAlarm(UserAlarm alarm, AlarmManager alarmManager, Context context) {
        if (alarm.isActive()) {
            scheduleAlarm(alarm, alarmManager, context);
        } else {
            cancelAlarm(alarm, alarmManager, context);
        }
    }

    private static void scheduleAlarm(UserAlarm alarm, AlarmManager alarmManager, Context context) {
        setAlarmStartTime(alarm);
        alarmManager.setRepeating(RTC_WAKEUP, calendar.getTimeInMillis(),INTERVAL_FIFTEEN_MINUTES / 15, getAlarmStartIntent(alarm, context));

        setAlarmEndTime(alarm, context);
        alarmManager.set(RTC, calendar.getTimeInMillis(), getAlarmStopIntent(alarm, context));
    }

    private static void cancelAlarm(UserAlarm alarm, AlarmManager alarmManager, Context context) {
        alarmManager.cancel(getAlarmStartIntent(alarm, context));
        alarmManager.cancel(getAlarmStopIntent(alarm, context));
    }

    private static PendingIntent getAlarmStartIntent(UserAlarm alarm, Context context) {
        Intent intent = new Intent(Intent.ACTION_INSERT, buildAlarmUri(alarm), context, ViewAlarms.class); // TODO receiver class goes here.
        return PendingIntent.getBroadcast(context, ALARM_START_REQUEST_CODE, intent, 0);
    }

    private static PendingIntent getAlarmStopIntent(UserAlarm alarm, Context context) {
        Intent intent = new Intent(Intent.ACTION_DELETE, buildAlarmUri(alarm), context, ViewAlarms.class); // TODO receiver class goes here.
        return PendingIntent.getBroadcast(context, ALARM_STOP_REQUEST_CODE, intent, 0);
    }

    private static void setAlarmStartTime(UserAlarm alarm) {
        calendar.set(Calendar.HOUR, alarm.getHour());
        calendar.set(Calendar.MINUTE, alarm.getMinutes());
    }

    private static void setAlarmEndTime(UserAlarm alarm, Context context) {
        String[] durations = context.getResources().getStringArray(R.array.duration_list);
        int durationType = IntStream.range(0, durations.length)
                .mapToObj(i -> (durations[i].equals(alarm.getDurationType())) ?
                        (i == 0 ? Calendar.HOUR : Calendar.MINUTE) : null)
                .filter(Objects::nonNull)
                .findFirst().orElse(-1);

        calendar.add(durationType, alarm.getDuration());
    }

    private static Uri buildAlarmUri(UserAlarm alarm) {
        return new Uri.Builder()
                .scheme("content")
                .path("alarms/" + alarm.getId())
                .build();
    }
}
