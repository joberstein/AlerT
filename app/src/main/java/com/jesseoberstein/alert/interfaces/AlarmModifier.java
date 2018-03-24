package com.jesseoberstein.alert.interfaces;

import com.jesseoberstein.alert.models.UserAlarm;

/**
 * An implementing activity should provide access to a draft alarm.
 */

public interface AlarmModifier {
    UserAlarm getDraftAlarm();
}
