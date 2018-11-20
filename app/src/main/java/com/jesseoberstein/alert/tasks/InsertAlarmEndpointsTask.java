package com.jesseoberstein.alert.tasks;

import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.models.AlarmEndpoint;
import com.jesseoberstein.alert.tasks.base.InsertTask;

public class InsertAlarmEndpointsTask extends InsertTask<AlarmEndpoint> {

    public InsertAlarmEndpointsTask(AppDatabase db) {
        super(db.alarmEndpointDao());
    }
}
