package com.jesseoberstein.alert.interfaces.data;

import com.jesseoberstein.alert.models.mbta.Route;

import java.util.List;

public interface RoutesReceiver {

    List<Route> getRouteList();

    void onReceiveRoutes(List<Route> routes);
}
