package com.jesseoberstein.alert.interfaces;

import com.jesseoberstein.alert.models.mbta.Direction;

/**
 * An implementing activity should set the stop and provide access to a draft alarm.
 */
public interface AlarmDirectionSetter extends AlarmModifier {
    void onAlarmDirectionSet(Direction direction);
}
