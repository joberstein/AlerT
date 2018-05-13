package com.jesseoberstein.alert.interfaces;

import com.jesseoberstein.alert.models.mbta.Endpoint;

import java.util.List;

/**
 * An implementing activity should set the selected day(s) and provide access to a draft alarm.
 */
public interface AlarmEndpointSetter extends AlarmModifier {
    void onAlarmEndpointsSet(List<Endpoint> endpoints);
}
