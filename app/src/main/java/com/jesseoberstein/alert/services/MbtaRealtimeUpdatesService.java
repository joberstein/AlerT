package com.jesseoberstein.alert.services;

import android.content.Intent;

import androidx.annotation.Nullable;

import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.interfaces.data.AlarmReceiver;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;
import com.jesseoberstein.alert.tasks.QueryAlarmsTask;
import com.jesseoberstein.alert.utils.MbtaRealtimeUpdatesHelper;
import com.jesseoberstein.alert.utils.UserAlarmScheduler;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import static com.jesseoberstein.alert.utils.Constants.ALARM_ID;

public class MbtaRealtimeUpdatesService extends AbstractService implements AlarmReceiver {

    @Inject
    AppDatabase db;

    @Inject
    UserAlarmScheduler alarmScheduler;

    @Inject
    MbtaRealtimeUpdatesHelper serviceHelper;

    public MbtaRealtimeUpdatesService() {
        super("MbtaRealtimeUpdatesService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Optional.ofNullable(intent)
                .map(i -> i.getLongExtra(ALARM_ID, -1))
                .ifPresent(id -> new QueryAlarmsTask(this, db, alarmScheduler).execute(id));
    }

    @Override
    public void onReceiveAlarms(List<UserAlarmWithRelations> alarms) {
        alarms.stream().findFirst().ifPresent(alarm -> serviceHelper.requestPrediction(alarm));
    }

    @Override
    public void onInsertAlarm(long insertedAlarmId) {}

    @Override
    public void onUpdateAlarm(UserAlarmWithRelations alarm) {}

    @Override
    public void onDeleteAlarm(UserAlarmWithRelations alarm) {}
}
