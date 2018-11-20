package com.jesseoberstein.alert.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.jesseoberstein.alert.data.dao.UserAlarmDao;
import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.interfaces.data.AlarmReceiver;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;

import java.util.Arrays;
import java.util.stream.Collectors;

public class UpdateAlarmTask extends AsyncTask<UserAlarmWithRelations, Void, Integer> {
    private final AlarmReceiver onAlarmUpdated;
    private final UserAlarmDao userAlarmDao;
    private UserAlarmWithRelations[] alarms;

    public UpdateAlarmTask(Context context, AppDatabase db) {
        this.onAlarmUpdated = (AlarmReceiver) context;
        this.userAlarmDao = db.userAlarmDao();
    }

    @Override
    protected Integer doInBackground(UserAlarmWithRelations[] alarmsWithRelations) {
        alarms = alarmsWithRelations;
        UserAlarm[] alarms = Arrays.stream(alarmsWithRelations)
                .map(UserAlarmWithRelations::getAlarm)
                .collect(Collectors.toList())
                .toArray(new UserAlarm[]{});

        return userAlarmDao.update(alarms);
    }

    @Override
    protected void onPostExecute(Integer numUpdated) {
        Log.i(this.getClass().getName(), "Updated " + numUpdated + " element(s).");
        Arrays.stream(alarms).forEach(onAlarmUpdated::onUpdateAlarm);
    }
}
