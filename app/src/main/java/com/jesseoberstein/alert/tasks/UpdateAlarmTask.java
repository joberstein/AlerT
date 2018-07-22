package com.jesseoberstein.alert.tasks;

import android.content.Context;

import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.interfaces.data.AlarmReceiver;
import com.jesseoberstein.alert.models.UserAlarm;

import java.util.List;

public class UpdateAlarmTask extends UpdateTask<UserAlarm> {
    private final AlarmReceiver onAlarmUpdated;

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
