package com.jesseoberstein.alert.activities.alarm;

import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.jesseoberstein.alert.data.AppDatabaseTest;
import com.jesseoberstein.alert.data.database.AppDatabase;

@VisibleForTesting
public class EditAlarmMock extends EditAlarm {

    @Override
    public AppDatabase getDatabase(Context context) {
        return AppDatabaseTest.getInstance(context);
    }
}
