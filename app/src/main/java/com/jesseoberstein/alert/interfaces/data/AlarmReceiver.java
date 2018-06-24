package com.jesseoberstein.alert.interfaces.data;

import com.jesseoberstein.alert.models.UserAlarm;

import java.util.List;

public interface AlarmReceiver {

    void onInsertAlarm(long insertedAlarmId);

    void onReceiveAlarms(List<UserAlarm> alarms);
}
