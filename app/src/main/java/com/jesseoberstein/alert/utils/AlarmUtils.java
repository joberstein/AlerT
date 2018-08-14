package com.jesseoberstein.alert.utils;

 import android.net.Uri;

import com.jesseoberstein.alert.models.AlarmEndpoint;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;

import java.util.Calendar;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AlarmUtils {
    private static Calendar calendar = Calendar.getInstance();

    public static long getAlarmStartTime(UserAlarm alarm) {
        calendar.set(Calendar.DAY_OF_WEEK, getNextFiringDay(alarm));
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        calendar.set(Calendar.MINUTE, alarm.getMinutes());
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static long getAlarmEndTime(UserAlarm alarm) {
        calendar.setTimeInMillis(getAlarmStartTime(alarm));
        calendar.add(Calendar.MINUTE, Long.valueOf(alarm.getDuration()).intValue());
        return calendar.getTimeInMillis();
    }

    public static Uri buildAlarmUri(long alarmId) {
        return new Uri.Builder().scheme("content").path("alarms/" + alarmId).build();
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

        return Stream.of(IntStream.range(today - 1, selectedDays.length), IntStream.range(0, today - 1))
                .flatMap(IntStream::boxed)
                .filter(i -> selectedDays[i] == 1)
                .filter(i -> !(today == (i + 1) && isPastAlarmFiringTime))
                .findFirst()
                .map(zeroIndexedDay -> zeroIndexedDay + 1)
                .orElseGet(() -> {
                    // At this point, either no days have been selected, or only today has been selected.
                    int tomorrow = DateTimeUtils.getTomorrow();
                    boolean isTodaySelected = alarm.getSelectedDays().isTodaySelected();
                    return !isTodaySelected && isPastAlarmFiringTime ? tomorrow : today;
                });
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
