package com.jesseoberstein.alert.providers;

import android.content.res.AssetManager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jesseoberstein.mbta.model.Route;
import com.jesseoberstein.mbta.model.Routes;
import com.jesseoberstein.mbta.model.Trip;
import com.jesseoberstein.mbta.utils.RouteLine;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides access to MBTA route data in many different forms.
 */
public class EndpointsProvider {
    private static EndpointsProvider instance = null;
    private static final ObjectMapper mapper = new ObjectMapper();
    private final AssetManager assetManager;

    private List<Routes> routeSchedules;

    private EndpointsProvider(AssetManager assetManager) {
        this.assetManager = assetManager;
        this.routeSchedules = this.parseScheduleByRoutesResponse();
    }

    /**
     * Initialize the EndpointsProvider if it doesn't already exist.
     * @param assetManager The android's context asset manager.
     * @return EndpointsProvider
     */
    public static EndpointsProvider init(AssetManager assetManager) {
        if (null == instance) {
            instance = new EndpointsProvider(assetManager);
        }
        return instance;
    }

    /**
     * Get all of the endpoints for a route.
     * @param parentRoute The parent route to get endpoints for.
     * @return A list of endpoint stop names.
     */
    public List<String> getEndpointsForRoute(String parentRoute) {
        return this.routeSchedules.stream()
                .filter(routes -> !routes.getModes().stream()
                        .filter(mode -> !mode.getRoutes().stream()
                                .filter(route -> route.getParentRoute().equals(parentRoute))
                                .collect(Collectors.toList())
                                .isEmpty())
                        .collect(Collectors.toList())
                        .isEmpty())
                .flatMap(routes -> routes.getModes().stream())
                .flatMap(mode -> mode.getRoutes().stream())
                .flatMap(route -> route.getDirections().stream())
                .flatMap(direction -> direction.getTrips().stream())
                .map(Trip::getEndpoint)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Route> getRoutesForStop(List<String> stopIds, List<String> routeNames) {
        return this.routeSchedules.stream()
                .flatMap(routes -> routes.getModes().stream())
                .flatMap(mode -> mode.getRoutes().stream())
                .filter(route -> routeNames.contains(route.getRouteName()) &&
                        !route.getDirections().stream()
                                .filter(direction -> !direction.getTrips().stream()
                                        .filter(trip -> !trip.getStops().stream()
                                                .filter(stop -> stopIds.contains(stop.getStopId()))
                                                .collect(Collectors.toList())
                                                .isEmpty())
                                        .collect(Collectors.toList())
                                        .isEmpty())
                                .collect(Collectors.toList())
                                .isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Get the endpoints for a stop, given its stop ids and the route it's on.
     * @param stopIds A list of inbound and outbound stop ids, specific to a route.
     * @return A list of endpoint names relative to the given stop.
     */
    public List<String> getEndpointsForStop(List<String> stopIds, String parentRoute) {
        return getEndpointsForStopFromRoutes(
                this.routeSchedules.stream()
                .flatMap(routes -> routes.getModes().stream())
                .flatMap(mode -> mode.getRoutes().stream())
                .filter(route -> route.getParentRoute().equals(parentRoute))
                .collect(Collectors.toList()), stopIds);
    }

    /**
     * Get the endpoints for a stop, given its stop ids and the specific route id.
     * @param stopIds A list of inbound and outbound stop ids, specific to the given route.
     * @param routeId A specific routeId.
     * @return A list of endpoint names relative to the given stop.
     */
    public List<String> getEndpointsForRouteStop(List<String> stopIds, String routeId) {
        return getEndpointsForStopFromRoutes(
                this.routeSchedules.stream()
                .flatMap(routes -> routes.getModes().stream())
                .flatMap(mode -> mode.getRoutes().stream())
                .filter(route -> route.getRouteId().equals(routeId))
                .collect(Collectors.toList()), stopIds);
    }

    /**
     * Return the actual Red Line endpoints (Ashmont or Braintree + Alewife).
     * @param line
     * @param endpoints
     * @return
     */
    public List<String> getRedLineEndpoints(RouteLine line, List<String> endpoints) {
        switch (line) {
            case A:
                return endpoints.stream().filter(e -> !e.equals("Braintree")).collect(Collectors.toList());
            case B:
                return endpoints.stream().filter(e -> !e.equals("Ashmont")).collect(Collectors.toList());
            default:
                return endpoints;
        }
    }

    private List<String> getEndpointsForStopFromRoutes(List<Route> routes, List<String> stopIds) {
        return routes.stream()
                .flatMap(route -> route.getDirections().stream())
                .flatMap(direction -> direction.getTrips().stream())
                .filter(trip -> !trip.getStops().stream()
                        .filter(stop -> stopIds.contains(stop.getStopId()))
                        .collect(Collectors.toList())
                        .isEmpty())
                .map(Trip::getEndpoint)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * This will parse the "routes_response.json" file into a Routes object and
     * get the modes from it.
     * @return A list of modes, which contains routes.
     */
    private List<Routes> parseScheduleByRoutesResponse() {
        List<Routes> routeSchedules = Collections.emptyList();
        try {
            InputStream schedulesResponse = this.assetManager.open("scheduleByRoutes_response.json");
            routeSchedules = mapper.readValue(schedulesResponse, new TypeReference<List<Routes>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return routeSchedules;
    }
}
