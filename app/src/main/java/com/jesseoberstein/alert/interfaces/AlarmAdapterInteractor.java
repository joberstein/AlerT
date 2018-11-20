package com.jesseoberstein.alert.interfaces;

import com.jesseoberstein.alert.models.UserAlarmWithRelations;

public interface AlarmAdapterInteractor {

    void onAlarmStatusToggle(UserAlarmWithRelations alarmWithRelations);
}
