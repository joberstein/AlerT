package com.jesseoberstein.alert.interfaces;

import com.jesseoberstein.alert.models.mbta.Stop;

/**
 * An implementing activity should set the stop and provide access to a draft alarm.
 */
public interface AlarmStopSetter extends AlarmModifier {
    void onAlarmStopSet(Stop stop);
}
