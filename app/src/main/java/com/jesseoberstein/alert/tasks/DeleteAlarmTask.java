package com.jesseoberstein.alert.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.jesseoberstein.alert.data.dao.AlarmEndpointDao;
import com.jesseoberstein.alert.data.dao.UserAlarmDao;
import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.interfaces.data.AlarmReceiver;
import com.jesseoberstein.alert.models.AlarmEndpoint;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;
import com.jesseoberstein.alert.tasks.base.DeleteTask;
import com.jesseoberstein.alert.tasks.base.InsertTask;
import com.jesseoberstein.alert.utils.IntentBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

public class DeleteAlarmTask extends AsyncTask<UserAlarmWithRelations, Void, Integer> {
    private final UserAlarmDao userAlarmDao;
    private final AlarmEndpointDao alarmEndpointDao;
    private final AlarmReceiver onAlarmDeleted;
    private UserAlarmWithRelations[] alarms;

    @Inject
    public DeleteAlarmTask(Context context, AppDatabase db) {
        userAlarmDao = db.userAlarmDao();
        alarmEndpointDao = db.alarmEndpointDao();
        this.onAlarmDeleted = (AlarmReceiver) context;
        alarms = new UserAlarmWithRelations[]{};
    }

    @Override
    protected Integer doInBackground(UserAlarmWithRelations[] alarmsToDelete) {
        alarms = alarmsToDelete;

        // Delete the alarm endpoints first.
        Arrays.stream(alarmsToDelete).forEach(alarm ->
            alarmEndpointDao.delete(alarm.getAlarmEndpoints().toArray(new AlarmEndpoint[]{})));

        // Then, get the list of alarms to delete.
        UserAlarm[] userAlarms = Arrays.stream(alarmsToDelete)
                .map(UserAlarmWithRelations::getAlarm)
                .collect(Collectors.toList())
                .toArray(new UserAlarm[]{});

        return userAlarmDao.delete(userAlarms);
    }

    @Override
    protected void onPostExecute(Integer numDeleted) {
        Arrays.stream(alarms).forEach(onAlarmDeleted::onDeleteAlarm);
        Log.i(UserAlarmDao.class.getName(), "Deleted " + numDeleted + " elements.");
    }
}
