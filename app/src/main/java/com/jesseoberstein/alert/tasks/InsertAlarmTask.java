package com.jesseoberstein.alert.tasks;

import android.content.Context;

import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.interfaces.data.AlarmReceiver;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.tasks.base.InsertTask;

import java.util.List;

import javax.inject.Inject;

public class InsertAlarmTask extends InsertTask<UserAlarm> {
    private final AlarmReceiver onAlarmInserted;

    @Inject
    public InsertAlarmTask(Context context, AppDatabase db) {
        super(db.userAlarmDao());
        this.onAlarmInserted = (AlarmReceiver) context;
    }

    @Override
    protected void onPostExecute(List<Long> insertedIds) {
        super.onPostExecute(insertedIds);
        // Only one alarm should've been inserted/updated.
        insertedIds.stream().findFirst().ifPresent(onAlarmInserted::onInsertAlarm);
    }
}
