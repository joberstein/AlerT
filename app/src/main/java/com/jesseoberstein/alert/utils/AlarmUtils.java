package com.jesseoberstein.alert.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.jesseoberstein.alert.models.AlarmEndpoint;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;
import com.jesseoberstein.alert.receivers.OnAlarmStart;
import com.jesseoberstein.alert.receivers.OnAlarmStop;
import com.jesseoberstein.alert.services.MbtaRealtimeUpdatesService;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static android.app.AlarmManager.INTERVAL_DAY;
import static android.app.AlarmManager.RTC;
import static android.app.AlarmManager.RTC_WAKEUP;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.app.PendingIntent.getBroadcast;
import static android.app.PendingIntent.getService;
import static com.jesseoberstein.alert.utils.Constants.ALARM_ID;
import static com.jesseoberstein.alert.utils.Constants.ALARM_START_REQUEST_CODE;
import static com.jesseoberstein.alert.utils.Constants.ALARM_STOP_REQUEST_CODE;
import static com.jesseoberstein.alert.utils.Constants.ALARM_UPDATE_REQUEST_CODE;
import static com.jesseoberstein.alert.utils.Constants.DAYS;
import static com.jesseoberstein.alert.utils.Constants.ENDPOINTS;
import static com.jesseoberstein.alert.utils.Constants.NICKNAME;
import static com.jesseoberstein.alert.utils.Constants.ROUTE;
import static com.jesseoberstein.alert.utils.Constants.STOP_ID;
import static java.util.Calendar.SATURDAY;
import static java.util.Calendar.SUNDAY;

public class AlarmUtils {
    private static Calendar calendar = Calendar.getInstance();

    public static boolean shouldAlarmFireToday(int[] selectedDays, Calendar calendar) {
        calendar.setTimeInMillis(System.currentTimeMillis());
        return selectedDays[calendar.get(Calendar.DAY_OF_WEEK)] == 1;
    }

    public static void scheduleOrCancelAlarm(UserAlarm alarm, List<String> endpoints, AlarmManager alarmManager, Context context, String stopId) {
        if (alarm.isActive()) {
            scheduleAlarm(alarm, endpoints, alarmManager, context, stopId);
        } else {
            cancelAlarm(alarm, alarmManager, context);
        }
    }

    private static void scheduleAlarm(UserAlarm alarm, List<String> endpoints, AlarmManager alarmManager, Context context, String stopId) {
        setAlarmStartTime(alarm);
        alarmManager.setRepeating(RTC_WAKEUP, calendar.getTimeInMillis(), INTERVAL_DAY, getAlarmStartIntent(alarm, endpoints, context, stopId));

        setAlarmEndTime(alarm, context);
        alarmManager.setInexactRepeating(RTC, calendar.getTimeInMillis(), INTERVAL_DAY, getAlarmStopIntent(alarm, context));
    }

    public static void cancelAlarm(UserAlarm alarm, AlarmManager alarmManager, Context context) {
        // Use an empty list/array for endpoints/stop ids because extras don't apply in the intent filter.
        alarmManager.cancel(getAlarmStartIntent(alarm, Collections.emptyList(), context, ""));
        alarmManager.cancel(getAlarmUpdateIntent(context, new Intent(Intent.ACTION_EDIT, buildAlarmUri(alarm.getId()))));
        alarmManager.cancel(getAlarmStopIntent(alarm, context));
    }

    public static PendingIntent getAlarmUpdateIntent(Context context, Intent receivedIntent) {
        Intent intent = new Intent(Intent.ACTION_EDIT, receivedIntent.getData(), context, MbtaRealtimeUpdatesService.class);
        Optional.ofNullable(receivedIntent.getExtras()).ifPresent(intent::putExtras);
        return getService(context, ALARM_UPDATE_REQUEST_CODE, intent, FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent getAlarmStartIntent(UserAlarm alarm, List<String> endpoints, Context context, String stopId) {
        Intent intent = new Intent(Intent.ACTION_INSERT, buildAlarmUri(alarm.getId()), context, OnAlarmStart.class);
        intent.putExtra(ROUTE, alarm.getRouteId());
        intent.putExtra(ALARM_ID, alarm.getId());
        intent.putExtra(NICKNAME, alarm.getNickname());
        intent.putExtra(DAYS, alarm.getSelectedDays().toIntArray());
        intent.putExtra(STOP_ID, stopId);
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
//        String[] durations = context.getResources().getStringArray(R.array.duration_list);
//        int durationType = IntStream.range(0, durations.length)
//                .mapToObj(i -> (durations[i].equals(alarm.getDurationType())) ?
//                        (i == 0 ? Calendar.HOUR : Calendar.MINUTE) : null)
//                .filter(Objects::nonNull)
//                .findFirst().orElse(-1);
//
//        calendar.add(durationType, alarm.getDuration());
    }

    private static Uri buildAlarmUri(long alarmId) {
        return new Uri.Builder()
                .scheme("content")
                .path("alarms/" + alarmId)
                .build();
    }

    /**
     * Get the day of the week that the given alarm should be fired next by taking into account the
     * alarm's hour/minutes and repeat days.
     * @param alarm The given alarm to calculate the next firing day with.
     * @return A {@link Calendar#DAY_OF_WEEK} value of the next firing day.
     */
    public static int getNextFiringDay(UserAlarm alarm) {
        int today = DateTimeUtils.getCurrentDay();
        long now = DateTimeUtils.getCurrentTimeInMillis();
        long alarmFiringTime = DateTimeUtils.getTimeInMillis(alarm.getHour(), alarm.getMinutes());
        boolean isPastAlarmFiringTime = alarmFiringTime <= now;
        int[] selectedDays = alarm.getSelectedDays().toIntArray();

        for (int day = today - 1; day < today + selectedDays.length; day++) {
            // Use an adjusted day to compensate for the offset between today and Sunday (0).
            int adjustedDay = day % 7;
            boolean isDaySelected = selectedDays[adjustedDay] == 1;
            boolean isDayToday = today == (adjustedDay + 1);

            if (isDaySelected) {
                if (isDayToday && isPastAlarmFiringTime) {
                    continue;
                }

                return adjustedDay + 1;
            }
        }

        // At this point, either no days have been selected, or only today has been selected.
        int tomorrow = (today == SATURDAY) ? SUNDAY : today + 1;
        boolean isTodaySelected = selectedDays[today - 1] == 1;
        return isPastAlarmFiringTime && !isTodaySelected ? tomorrow : today;
    }


    /**
     * Map an alarm's id and endpoints to a list of {@link AlarmEndpoint}.
     * @param alarmWithRelations containing an id and endpoints
     * @return a list of alarm endpoints
     */
    public static AlarmEndpoint[] createAlarmEndpoints(UserAlarmWithRelations alarmWithRelations) {
        return alarmWithRelations.getEndpoints().stream()
                .map(endpoint -> new AlarmEndpoint(alarmWithRelations.getAlarm().getId(), endpoint.getId()))
                .toArray(AlarmEndpoint[]::new);
    }
}
