package com.jesseoberstein.alert.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;

import com.jesseoberstein.alert.models.UserAlarm;

import java.util.Arrays;

import javax.inject.Inject;

import static android.app.AlarmManager.RTC_WAKEUP;

public class AlarmManagerHelper {
    private final IntentBuilder intentBuilder;
    private final AlarmManager alarmManager;
    private final UserAlarmScheduler userAlarmScheduler;

    @Inject
    public AlarmManagerHelper(IntentBuilder intentBuilder, AlarmManager alarmManager, UserAlarmScheduler userAlarmScheduler) {
        this.intentBuilder = intentBuilder;
        this.alarmManager = alarmManager;
        this.userAlarmScheduler = userAlarmScheduler;
    }

    public void scheduleUserAlarm(UserAlarm alarm) {
        cancelAlarm(alarm);  // Ensure that this alarm isn't scheduled twice.
        scheduleAlarm(alarm);
    }

    public void cancelUserAlarm(UserAlarm alarm) {
        cancelAlarm(alarm);
    }

    private void scheduleAlarm(UserAlarm alarm) {
        long startTime = userAlarmScheduler.getAlarmStartTime(alarm);

        PendingIntent startIntent = intentBuilder.getAlarmStartIntent(alarm.getId());
        alarmManager.setExactAndAllowWhileIdle(RTC_WAKEUP, startTime, startIntent);
    }

    private void cancelAlarm(UserAlarm alarm) {
        long alarmId = alarm.getId();
        Arrays.asList(
            intentBuilder.getAlarmStartIntent(alarmId),
            intentBuilder.getAlarmStopIntent(alarmId)
        ).forEach(alarmManager::cancel);
    }
}
