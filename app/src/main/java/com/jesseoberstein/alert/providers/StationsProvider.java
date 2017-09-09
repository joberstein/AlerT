package com.jesseoberstein.alert.providers;

import android.content.res.AssetManager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jesseoberstein.mbta.model.Directions;
import com.jesseoberstein.mbta.model.Stop;
import com.jesseoberstein.mbta.utils.RouteName;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StationsProvider {
    private static StationsProvider instance = null;
    private static final ObjectMapper mapper = new ObjectMapper();
    private final AssetManager assetManager;
    private final List<Directions> directions;

    private StationsProvider(AssetManager assetManager) {
        this.assetManager = assetManager;
        this.directions = this.parseRoutesByStationsResponse();
    }

    /**
     * Initialize the StationsProvider if it doesn't already exist.
     * @param assetManager The android's context asset manager.
     * @return StationsProvider
     */
    public static StationsProvider init(AssetManager assetManager) {
        if (null == instance) {
            instance = new StationsProvider(assetManager);
        }
        return instance;
    }


    /**
     * Get all of the stop names for a given route.
     * @param parentRoute The parent route to get the stops for.
     * @return A list of stop names in the given route.
     */
    public List<String> getStopNamesForRoute(String parentRoute) {
        return getStopsForRoute(parentRoute).stream()
                .map(Stop::getRealStopName)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Get all of the stops for a given route.
     * @param parentRoute The parent route to get the stops for.
     * @return A list of stop in the given route (contains inbound and outbound stops).
     */
    public List<Stop> getStopsForRoute(String parentRoute) {
        return this.directions.stream()
                .filter(dirs -> dirs.getRoute().getParentRoute().equals(parentRoute))
                .flatMap(dirs -> dirs.getDirections().stream())
                .flatMap(direction -> direction.getStops().stream())
                .collect(Collectors.toList());
    }

    /**
     * Get the stop ids for the given stop given a parent route.  This is useful because for some
     * reason, the same stop has a different inbound and outbound stop id.
     * @param stopName The name of the stop to get the ids for.
     * @param parentRoute The name of the parent route.
     * @return A list of stop ids.
     */
    public List<String> getStopIdsForStop(String stopName, String parentRoute) {
        return getStopsForRoute(parentRoute).stream()
                .filter(stop -> stop.getRealStopName().equals(stopName))
                .map(Stop::getStopId)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * This will parse the "routes_response.json" file into a Routes object and
     * get the modes from it.
     * @return A list of modes, which contains routes.
     */
    private List<Directions> parseRoutesByStationsResponse() {
        List<Directions> directions = Collections.emptyList();
        try {
            InputStream directionsResponse = this.assetManager.open("stopsByRoute_response.json");
            directions = mapper.readValue(directionsResponse, new TypeReference<List<Directions>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return directions.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }
}
