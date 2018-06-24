package com.jesseoberstein.alert.tasks;

import android.content.Context;

import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.interfaces.data.AlarmReceiver;
import com.jesseoberstein.alert.models.UserAlarm;

import java.util.Arrays;
import java.util.List;

public class InsertAlarmTask extends InsertTask<UserAlarm> {
    private final AlarmReceiver onAlarmInserted;

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
