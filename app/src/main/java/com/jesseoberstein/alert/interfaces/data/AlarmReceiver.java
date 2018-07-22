package com.jesseoberstein.alert.interfaces.data;

import com.jesseoberstein.alert.models.UserAlarmWithRelations;

import java.util.List;

public interface AlarmReceiver {

    void onInsertAlarm(long insertedAlarmId);

    void onUpdateAlarm(boolean isUpdated);

    void onReceiveAlarms(List<UserAlarmWithRelations> alarms);
}
