package com.jesseoberstein.alert.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.VisibleForTesting;

import com.jesseoberstein.alert.data.dao.DirectionDao;
import com.jesseoberstein.alert.data.dao.RouteDao;
import com.jesseoberstein.alert.data.dao.StopDao;
import com.jesseoberstein.alert.data.dao.UserAlarmDao;
import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.interfaces.data.AlarmReceiver;
import com.jesseoberstein.alert.models.UserAlarm;

import java.util.List;

public class QueryAlarmsTask extends AsyncTask<Void, Void, List<UserAlarm>> {
    private final AppDatabase db;
    private final AlarmReceiver alarmReceiver;

    public QueryAlarmsTask(Context context, AppDatabase db) {
        this.db = db;
        this.alarmReceiver = (AlarmReceiver) context;
    }

    @Override
    protected List<UserAlarm> doInBackground(Void[] dummyParam) {
        List<UserAlarm> userAlarms = this.db.userAlarmDao().getAll();
        userAlarms.forEach(alarm -> {
            alarm.setRoute(this.db.routeDao().get(alarm.getRouteId()));
            alarm.setStop(this.db.stopDao().get(alarm.getStopId()));
            alarm.setDirection(this.db.directionDao().get(alarm.getDirectionId(), alarm.getRouteId()));
        });
        return userAlarms;
    }

    @Override
    protected void onPostExecute(List<UserAlarm> alarms) {
        this.alarmReceiver.onReceiveAlarms(alarms);
    }
}
