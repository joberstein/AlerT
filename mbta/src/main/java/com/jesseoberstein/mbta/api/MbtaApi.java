package com.jesseoberstein.mbta.api;

import com.jesseoberstein.mbta.model.Endpoint;
import com.jesseoberstein.mbta.model.Prediction;
import com.jesseoberstein.mbta.model.Route;
import com.jesseoberstein.mbta.model.Stop;
import com.jesseoberstein.mbta.model.Trip;

import java.util.List;
import java.util.Locale;

import static com.jesseoberstein.mbta.api.Fetch.fetch;

public class MbtaApi {

    public static List<Route> fetchRoutes() {
        return fetch("routes", "", Route.class);
    }

    public static List<Stop> fetchStopsByRoute(String routeId) {
        return fetch("stops", "filter[route]=" + routeId, Stop.class);
    }

    public static List<Trip> fetchTripsByRoute(String routeId) {
        return fetch("trips", "filter[route]=" + routeId, Trip.class);
    }

    public static List<Prediction> fetchPredictionsForStopOnRouteToEndpoint(String routeId, String stopId, int directionId) {
        String queryPattern = "filter[route]=%s&filter[stop]=%s&filter[direction_id]=%d&include=trip";
        String queryString = String.format(Locale.US, queryPattern, routeId, stopId, directionId);
        return fetch("predictions", queryString, Prediction.class);
    }
}