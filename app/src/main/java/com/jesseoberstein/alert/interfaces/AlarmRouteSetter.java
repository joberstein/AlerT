package com.jesseoberstein.alert.interfaces;

import com.jesseoberstein.alert.models.UserRoute;

/**
 * An implementing activity should set the route and provide access to a draft alarm.
 */
public interface AlarmRouteSetter extends AlarmModifier {
    void onAlarmRouteSet(UserRoute route);
}