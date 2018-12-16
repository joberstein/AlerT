package com.jesseoberstein.alert.utils;

import android.content.Context;

import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.interfaces.data.AlarmReceiver;
import com.jesseoberstein.alert.models.RepeatType;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;
import com.jesseoberstein.alert.tasks.QueryAlarmsTask;
import com.jesseoberstein.alert.tasks.UpdateAlarmTask;

import java.util.List;

import javax.inject.Inject;

public class OnAlarmStopHelper implements AlarmReceiver {

    private final Context context;
    private final UserAlarmScheduler userAlarmScheduler;
    private final AppDatabase database;

    @Inject
    public OnAlarmStopHelper(Context context, UserAlarmScheduler userAlarmScheduler, AppDatabase database) {
        this.context = context;
        this.userAlarmScheduler = userAlarmScheduler;
        this.database = database;
    }

    public void deactivateSingleFireAlarm(long alarmId) {
        new QueryAlarmsTask(context, database, userAlarmScheduler).execute(alarmId);
    }


    @Override
    public void onReceiveAlarms(List<UserAlarmWithRelations> alarms) {
        alarms.stream().findFirst().ifPresent(alarmWithRelations -> {
            UserAlarm alarm = alarmWithRelations.getAlarm();
            if (RepeatType.NEVER.equals(alarm.getRepeatType())) {
                alarm.setActive(false);
            }
            alarmWithRelations.setAlarm(alarm);
            new UpdateAlarmTask(context, database).execute(alarmWithRelations);
        });
    }

    @Override
    public void onDeleteAlarm(UserAlarmWithRelations alarm) {}

    @Override
    public void onInsertAlarm(long insertedAlarmId) {}

    @Override
    public void onUpdateAlarm(UserAlarmWithRelations alarm) {}
}
