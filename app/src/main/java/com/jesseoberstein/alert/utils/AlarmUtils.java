package com.jesseoberstein.alert.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.receivers.OnAlarmStart;
import com.jesseoberstein.alert.receivers.OnAlarmStop;
import com.jesseoberstein.alert.services.MbtaRealtimeUpdatesService;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

import static android.app.AlarmManager.INTERVAL_DAY;
import static android.app.AlarmManager.RTC;
import static android.app.AlarmManager.RTC_WAKEUP;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.app.PendingIntent.getBroadcast;
import static android.app.PendingIntent.getService;
import static com.jesseoberstein.alert.utils.Constants.*;
import static com.jesseoberstein.alert.utils.Constants.ALARM_START_REQUEST_CODE;
import static com.jesseoberstein.alert.utils.Constants.ALARM_STOP_REQUEST_CODE;

public class AlarmUtils {
    private static Calendar calendar = Calendar.getInstance();

    public static boolean shouldAlarmFireToday(int[] selectedDays, Calendar calendar) {
        calendar.setTimeInMillis(System.currentTimeMillis());
        return selectedDays[calendar.get(Calendar.DAY_OF_WEEK)] == 1;
    }

    public static void scheduleOrCancelAlarm(UserAlarm alarm, List<String> endpoints, AlarmManager alarmManager, Context context, String[] stopIds) {
        if (alarm.isActive()) {
            scheduleAlarm(alarm, endpoints, alarmManager, context, stopIds);
        } else {
            cancelAlarm(alarm, alarmManager, context);
        }
    }

    private static void scheduleAlarm(UserAlarm alarm, List<String> endpoints, AlarmManager alarmManager, Context context, String[] stopIds) {
        setAlarmStartTime(alarm);
        alarmManager.setRepeating(RTC_WAKEUP, calendar.getTimeInMillis(), INTERVAL_DAY, getAlarmStartIntent(alarm, endpoints, context, stopIds));

        setAlarmEndTime(alarm, context);
        alarmManager.setInexactRepeating(RTC, calendar.getTimeInMillis(), INTERVAL_DAY, getAlarmStopIntent(alarm, context));
    }

    public static void cancelAlarm(UserAlarm alarm, AlarmManager alarmManager, Context context) {
        // Use an empty list/array for endpoints/stop ids because extras don't apply in the intent filter.
        alarmManager.cancel(getAlarmStartIntent(alarm, Collections.emptyList(), context, new String[]{}));
        alarmManager.cancel(getAlarmUpdateIntent(context, new Intent(Intent.ACTION_EDIT, buildAlarmUri(alarm.getId()))));
        alarmManager.cancel(getAlarmStopIntent(alarm, context));
    }

    public static PendingIntent getAlarmUpdateIntent(Context context, Intent receivedIntent) {
        Intent intent = new Intent(Intent.ACTION_EDIT, receivedIntent.getData(), context, MbtaRealtimeUpdatesService.class);
        Optional.ofNullable(receivedIntent.getExtras()).ifPresent(intent::putExtras);
        return getService(context, ALARM_UPDATE_REQUEST_CODE, intent, FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent getAlarmStartIntent(UserAlarm alarm, List<String> endpoints, Context context, String[] stopIds) {
        Intent intent = new Intent(Intent.ACTION_INSERT, buildAlarmUri(alarm.getId()), context, OnAlarmStart.class);
        intent.putExtra(ROUTE, alarm.getRoute().getRouteName());
        intent.putExtra(ALARM_ID, alarm.getId());
        intent.putExtra(NICKNAME, alarm.getNickname());
        intent.putExtra(COLOR, AlertUtils.getRouteResource(alarm.getRoute().getRouteName(), COLOR));
        intent.putExtra(DAYS, alarm.getWeekdays());
        intent.putExtra(STOP_IDS, stopIds);
        intent.putExtra(ENDPOINTS, endpoints.toArray(new String[]{}));
        return getBroadcast(context, ALARM_START_REQUEST_CODE, intent, FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent getAlarmStopIntent(UserAlarm alarm, Context context) {
        Intent intent = new Intent(Intent.ACTION_DELETE, buildAlarmUri(alarm.getId()), context, OnAlarmStop.class);
        return getBroadcast(context, ALARM_STOP_REQUEST_CODE, intent, FLAG_UPDATE_CURRENT);
    }

    private static void setAlarmStartTime(UserAlarm alarm) {
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        calendar.set(Calendar.MINUTE, alarm.getMinutes());
        calendar.set(Calendar.SECOND, 0);
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

    private static Uri buildAlarmUri(int alarmId) {
        return new Uri.Builder()
                .scheme("content")
                .path("alarms/" + alarmId)
                .build();
    }
}
