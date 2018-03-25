package com.jesseoberstein.alert.interfaces;

import com.jesseoberstein.alert.models.RepeatType;

/**
 * An implementing activity should set the repeat type and provide access to a draft alarm.
 */
public interface AlarmRepeatSetter extends AlarmModifier {
    void onAlarmRepeatSet(RepeatType repeatType);
}
