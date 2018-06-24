package com.jesseoberstein.alert.activities.alarms;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.VisibleForTesting;
import android.view.View;

import com.jesseoberstein.alert.activities.alarm.EditAlarmMock;
import com.jesseoberstein.alert.data.AppDatabaseTest;
import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.listeners.StartActivityOnClick;

@VisibleForTesting
public class ViewAlarmsMock extends ViewAlarms {

    @Override
    public AppDatabase getDatabase(Context context) {
        return AppDatabaseTest.getInstance(context);
    }

    @Override
    public View.OnClickListener getOnCreateAlarmClick() {
        return new StartActivityOnClick(this, EditAlarmMock.class)
                .withAction(Intent.ACTION_INSERT);
    }
}
