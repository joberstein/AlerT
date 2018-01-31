package com.jesseoberstein.mbta;

import com.github.jasminb.jsonapi.exceptions.DocumentSerializationException;
import com.jesseoberstein.mbta.model.Endpoint;
import com.jesseoberstein.mbta.model.Route;
import com.jesseoberstein.mbta.model.Stop;
import com.jesseoberstein.mbta.utils.ResponseParser;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.jesseoberstein.mbta.api.MbtaApi.fetchRoutes;
import static com.jesseoberstein.mbta.api.MbtaApi.fetchStopsByRoute;
import static com.jesseoberstein.mbta.api.MbtaApi.fetchTripsByRoute;

public class FetchMbtaData {

    public static void main(String[] args) throws DocumentSerializationException {
        List<Route> routes = fetchRoutes();

        ResponseParser.writeJSONApi(routes, "routes");
        ResponseParser.writeJSONApi(getStops(routes), "stops");
        ResponseParser.write(getEndpoints(routes), "endpoints");
    }

    private static List<Stop> getStops(List<Route> routes) {
        return routes.stream()
                .map(route -> {
                    List<Stop> stopsForRoute = fetchStopsByRoute(route.getId());
                    stopsForRoute.forEach(stop -> stop.setRouteId(route.getId()));
                    return stopsForRoute;
                })
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    private static List<Endpoint> getEndpoints(List<Route> routes) {
        return routes.stream()
                .flatMap(route -> fetchTripsByRoute(route.getId()).stream()
                        .filter(trip -> trip.getRoute().getId().equals(route.getId()))
                        .map(trip -> new Endpoint(
                                trip.getHeadsign(),
                                route.getId(),
                                trip.getDirectionId(),
                                route.getDirectionNames().get(trip.getDirectionId())))
                        .collect(Collectors.toList())
                        .stream())
                .distinct()
                .collect(Collectors.toList());
    }
}
