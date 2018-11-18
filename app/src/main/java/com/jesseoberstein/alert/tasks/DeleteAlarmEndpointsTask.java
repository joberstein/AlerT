package com.jesseoberstein.alert.tasks;

import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.models.AlarmEndpoint;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.tasks.base.InsertTask;

import javax.inject.Inject;

public class DeleteAlarmEndpointsTask extends InsertTask<AlarmEndpoint> {

    @Inject
    public DeleteAlarmEndpointsTask(AppDatabase db) {
        super(db.alarmEndpointDao());
    }
}