package com.jesseoberstein.alert.interfaces.data;

import com.jesseoberstein.alert.models.mbta.Direction;
import com.jesseoberstein.alert.models.mbta.Route;

import java.util.List;

public interface DirectionsReceiver {

    List<Direction> getDirectionList();

    void onReceiveDirections(List<Direction> directions);
}
