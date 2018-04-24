package com.jesseoberstein.mbta;

import com.github.jasminb.jsonapi.exceptions.DocumentSerializationException;
import com.jesseoberstein.mbta.model.Direction;
import com.jesseoberstein.mbta.model.Endpoint;
import com.jesseoberstein.mbta.model.Route;
import com.jesseoberstein.mbta.model.Stop;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.jesseoberstein.mbta.api.MbtaApi.fetchRoutes;
import static com.jesseoberstein.mbta.api.MbtaApi.fetchStopsByRoute;
import static com.jesseoberstein.mbta.api.MbtaApi.fetchTripsByRoute;
import static com.jesseoberstein.mbta.utils.ResponseParser.writeToFile;

public class FetchMbtaData {

    public static void main(String[] args) throws DocumentSerializationException {
        Collection<Route> routes = fetchRoutes();

        writeToFile(stringifyRoutes(routes), "routes");
        writeToFile(stringifyStops(routes), "stops");
        writeToFile(getEndpoints(routes), "endpoints");
    }

    private static String stringifyRoutes(Collection<Route> routes) {
        return routes.parallelStream()
                .map(Route::getValues)
                .collect(Collectors.joining("\n"));
    }

    private static String stringifyStops(Collection<Route> routes) {
        Map<String, Stop> stopsMap = new HashMap<>();
        routes.parallelStream()
            .map(route -> route.getId())
            .flatMap(routeId -> fetchStopsByRoute(routeId).stream())
            .forEach(stop -> {
                String stopId = stop.getId();
                if (!stopsMap.containsKey(stopId)) {
                    stopsMap.put(stopId, stop);
                } else {
                    stop.getRouteIds().forEach(routeId -> stopsMap.get(stopId).addRouteId(routeId));
                }
            });

        return stopsMap.values().parallelStream()
            .map(Stop::getValues)
            .collect(Collectors.joining("\n"));
    }

    private static List<Endpoint> getEndpoints(Collection<Route> routes) {
        return routes.stream()
                .flatMap(route -> fetchTripsByRoute(route.getId()).stream()
                        .filter(trip -> trip.getRoute().getId().equals(route.getId()))
                        .map(trip -> new Endpoint(
                                trip.getHeadsign(),
                                new Direction(
                                        trip.getDirectionId(),
                                        route.getDirectionNames().get(trip.getDirectionId()),
                                        route.getId())))
                        .collect(Collectors.toList())
                        .stream())
                .distinct()
                .collect(Collectors.toList());
    }
}
