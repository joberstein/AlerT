package com.jesseoberstein.alert.interfaces;

/**
 * An implementing activity should set the duration and provide access to a draft alarm.
 */
public interface AlarmDurationSetter extends AlarmModifier {
    void onAlarmDurationSet(long duration);
}
