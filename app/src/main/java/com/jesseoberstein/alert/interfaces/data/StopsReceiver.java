package com.jesseoberstein.alert.interfaces.data;

import com.jesseoberstein.alert.models.mbta.Stop;

import java.util.List;

public interface StopsReceiver {

    List<Stop> getStopList();

    void onReceiveStops(List<Stop> stops);
}
