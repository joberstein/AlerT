package com.jesseoberstein.alert.interfaces.data;

import com.jesseoberstein.alert.models.UserAlarmWithRelations;

import java.util.List;

public interface AlarmReceiver {

    void onInsertAlarm(long insertedAlarmId);

    void onUpdateAlarm(UserAlarmWithRelations alarm);

    void onReceiveAlarms(List<UserAlarmWithRelations> alarms);

    void onDeleteAlarm(UserAlarmWithRelations alarm);
}
