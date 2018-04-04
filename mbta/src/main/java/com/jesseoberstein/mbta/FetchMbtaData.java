package com.jesseoberstein.mbta;

import com.github.jasminb.jsonapi.exceptions.DocumentSerializationException;
import com.jesseoberstein.mbta.model.Endpoint;
import com.jesseoberstein.mbta.model.Route;
import com.jesseoberstein.mbta.utils.ResponseParser;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.jesseoberstein.mbta.api.MbtaApi.fetchRoutes;
import static com.jesseoberstein.mbta.api.MbtaApi.fetchStopsByRoute;
import static com.jesseoberstein.mbta.api.MbtaApi.fetchTripsByRoute;
import static com.jesseoberstein.mbta.utils.ResponseParser.*;

public class FetchMbtaData {

    public static void main(String[] args) throws DocumentSerializationException {
        List<Route> routes = fetchRoutes();

        writeJSONApiToFile(routes, "routes");
        writeToFile(getStops(routes), "stops");
        writeToFile(getEndpoints(routes), "endpoints");
    }

    private static Map<String, byte[]> getStops(List<Route> routes) {
        return routes.stream().collect(Collectors.toMap(
                route -> route.getId(),
                route -> ResponseParser.writeJSONApiToBytes(fetchStopsByRoute(route.getId()))));
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
