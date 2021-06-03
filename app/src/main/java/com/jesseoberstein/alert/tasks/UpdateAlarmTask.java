package com.jesseoberstein.alert.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.jesseoberstein.alert.data.dao.UserAlarmDao;
import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.interfaces.data.AlarmReceiver;
import com.jesseoberstein.alert.models.UserAlarm;

import java.util.Arrays;

public class UpdateAlarmTask extends AsyncTask<UserAlarm, Void, Integer> {
    private final AlarmReceiver onAlarmUpdated;
    private final UserAlarmDao userAlarmDao;
    private UserAlarm[] alarms;

    public UpdateAlarmTask(Context context, AppDatabase db) {
        this.onAlarmUpdated = (AlarmReceiver) context;
        this.userAlarmDao = db.userAlarmDao();
    }

    @Override
    protected Integer doInBackground(UserAlarm[] alarms) {
        this.alarms = alarms;
        return userAlarmDao.update(alarms);
    }

    @Override
    protected void onPostExecute(Integer numUpdated) {
        Log.i(this.getClass().getName(), "Updated " + numUpdated + " element(s).");
        Arrays.stream(alarms).forEach(onAlarmUpdated::onUpdateAlarm);
    }
}
