package com.jesseoberstein.alert.interfaces;

/**
 * An implementing activity should set the selected day(s) and provide access to a draft alarm.
 */
public interface AlarmDaySetter extends AlarmModifier {
    void onAlarmDaysSet(int[] days);
}
