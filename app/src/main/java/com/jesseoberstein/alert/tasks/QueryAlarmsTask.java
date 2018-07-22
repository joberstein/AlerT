package com.jesseoberstein.alert.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.jesseoberstein.alert.data.dao.AlarmEndpointDao;
import com.jesseoberstein.alert.data.dao.UserAlarmDao;
import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.interfaces.data.AlarmReceiver;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;

import java.util.Arrays;
import java.util.List;

public class QueryAlarmsTask extends AsyncTask<Long, Void, List<UserAlarmWithRelations>> {
    private final AppDatabase db;
    private final AlarmReceiver alarmReceiver;

    public QueryAlarmsTask(Context context, AppDatabase db) {
        this.db = db;
        this.alarmReceiver = (AlarmReceiver) context;
    }

    @Override
    protected List<UserAlarmWithRelations> doInBackground(Long[] alarmIds) {
        UserAlarmDao alarmDao = this.db.userAlarmDao();
        AlarmEndpointDao alarmEndpointDao = this.db.alarmEndpointDao();
        boolean shouldQueryAll = Arrays.asList(alarmIds).isEmpty();

        List<UserAlarmWithRelations> alarms = shouldQueryAll ?
                alarmDao.getAllWithRelations() :
                alarmDao.getWithRelations(alarmIds);

        alarms.forEach(alarm -> {
            long alarmId = alarm.getAlarm().getId();
            alarm.setEndpoints(alarmEndpointDao.getEndpointsByAlarm(alarmId));
        });

        return alarms;
    }

    @Override
    protected void onPostExecute(List<UserAlarmWithRelations> alarms) {
        this.alarmReceiver.onReceiveAlarms(alarms);
    }
}
