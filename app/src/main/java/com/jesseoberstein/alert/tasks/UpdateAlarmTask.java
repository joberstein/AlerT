package com.jesseoberstein.alert.tasks;

import android.content.Context;

import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.interfaces.data.AlarmReceiver;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.tasks.base.UpdateTask;

import javax.inject.Inject;

public class UpdateAlarmTask extends UpdateTask<UserAlarm> {
    private final AlarmReceiver onAlarmUpdated;

    @Inject
    public UpdateAlarmTask(Context context, AppDatabase db) {
        super(db.userAlarmDao());
        this.onAlarmUpdated = (AlarmReceiver) context;
    }

    @Override
    protected void onPostExecute(Integer numUpdated) {
        super.onPostExecute(numUpdated);
        // Only one alarm should've been inserted/updated.
        onAlarmUpdated.onUpdateAlarm(numUpdated == 1);
    }
}
