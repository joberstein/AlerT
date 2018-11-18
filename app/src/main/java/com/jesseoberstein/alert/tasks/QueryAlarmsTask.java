package com.jesseoberstein.alert.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.jesseoberstein.alert.data.dao.AlarmEndpointDao;
import com.jesseoberstein.alert.data.dao.UserAlarmDao;
import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.interfaces.data.AlarmReceiver;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;
import com.jesseoberstein.alert.utils.UserAlarmScheduler;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

public class QueryAlarmsTask extends AsyncTask<Long, Void, List<UserAlarmWithRelations>> {
    private final AppDatabase db;
    private final AlarmReceiver alarmReceiver;
    private final UserAlarmScheduler userAlarmScheduler;

    public QueryAlarmsTask(Context context, AppDatabase db, UserAlarmScheduler userAlarmScheduler) {
        this.db = db;
        this.alarmReceiver = (AlarmReceiver) context;
        this.userAlarmScheduler = userAlarmScheduler;
    }

    @Override
    protected List<UserAlarmWithRelations> doInBackground(Long[] alarmIds) {
        UserAlarmDao alarmDao = this.db.userAlarmDao();
        AlarmEndpointDao alarmEndpointDao = this.db.alarmEndpointDao();
        boolean shouldQueryAll = Arrays.asList(alarmIds).isEmpty();

        List<UserAlarmWithRelations> alarms = shouldQueryAll ?
                alarmDao.getAllWithRelations() :
                alarmDao.getWithRelations(alarmIds);

        // Set the endpoints and time for each alarm because they aren't stored with it in the db.
        alarms.forEach(alarm -> {
            long alarmId = alarm.getAlarm().getId();
            alarm.setEndpoints(alarmEndpointDao.getEndpointsByAlarm(alarmId));
            userAlarmScheduler.restoreAlarmTime(alarm.getAlarm());
        });

        return alarms;
    }

    @Override
    protected void onPostExecute(List<UserAlarmWithRelations> alarms) {
        this.alarmReceiver.onReceiveAlarms(alarms);
    }
}
