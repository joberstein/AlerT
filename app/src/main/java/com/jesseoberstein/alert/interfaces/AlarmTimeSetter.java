package com.jesseoberstein.alert.interfaces;

/**
 * An implementing activity should set the new time and provide access to a draft alarm.
 */
public interface AlarmTimeSetter extends AlarmModifier {
    void onAlarmTimeSet(int hour, int minute);
}
