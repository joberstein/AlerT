package com.jesseoberstein.alert.providers;

import android.content.res.AssetManager;

import com.jesseoberstein.mbta.model.Stop;
import com.jesseoberstein.mbta.utils.ResponseParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.jesseoberstein.alert.utils.Constants.MBTA_DATA_DATE;

public class StopsProvider {

    private List<Stop> stops;

    public StopsProvider(AssetManager assetManager, String routeId) {
        serializeStops(assetManager, routeId);
    }

    public List<Stop> getStops() {
        return this.stops;
    }

    /**
     * Get all of the stop names for a given route.
     * @return A list of stop names in the given route.
     */
    public List<String> getStopNames() {
        return this.stops.parallelStream().map(Stop::getName).collect(Collectors.toList());
    }

    public Stop getStopById(String stopId) {
        return this.stops.stream().filter(stop -> stop.getId().equals(stopId)).findFirst().orElse(null);
    }

    private void serializeStops(AssetManager assetManager, String routeId) {
        if (stops != null) {
            return;
        }

        try {
            InputStream inputStream = assetManager.open( MBTA_DATA_DATE +"/stops.json");
            stops = ResponseParser.parseJSONApi(inputStream, Stop.class).stream()
                    .filter(stop -> stop.getRouteIds().contains(routeId))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            stops = Collections.emptyList();
        }
    }
}
