package com.jesseoberstein.alert.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;

import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;

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

    public void scheduleUserAlarm(UserAlarmWithRelations alarmWithRelations) {
        cancelAlarm(alarmWithRelations);  // Ensure that this alarm isn't scheduled twice.
        scheduleAlarm(alarmWithRelations);
    }

    public void cancelUserAlarm(UserAlarmWithRelations alarmWithRelations) {
        cancelAlarm(alarmWithRelations);
    }

    private void scheduleAlarm(UserAlarmWithRelations alarmWithRelations) {
        UserAlarm alarm = alarmWithRelations.getAlarm();
        long startTime = userAlarmScheduler.getAlarmStartTime(alarm);

        PendingIntent startIntent = intentBuilder.getAlarmStartIntent(alarm.getId());
        alarmManager.setExactAndAllowWhileIdle(RTC_WAKEUP, startTime, startIntent);
    }

    private void cancelAlarm(UserAlarmWithRelations alarmWithRelations) {
        long alarmId = alarmWithRelations.getAlarm().getId();
        Arrays.asList(
            intentBuilder.getAlarmStartIntent(alarmId),
            intentBuilder.getAlarmStopIntent(alarmId)
        ).forEach(alarmManager::cancel);
    }
}
