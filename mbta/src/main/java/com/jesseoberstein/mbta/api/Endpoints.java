package com.jesseoberstein.mbta.api;

import com.jesseoberstein.mbta.model.Directions;
import com.jesseoberstein.mbta.model.Routes;

import java.util.List;
import java.util.Optional;

import static com.jesseoberstein.mbta.api.Fetch.fetch;

public class Endpoints {

    public static Optional<Routes> fetchRoutes() {
        return fetch("routes", "", Routes.class);
    }

    public static Optional<Directions> fetchStopsByRoute(String route) {
        return fetch("stopsByRoute", "route=" + route, Directions.class);
    }

    /**
     * Fetch schedules by routes.  Since the purpose of this API call is to get a route's endpoints,
     * limit the trips to 1 for either direction to minimize the amount of data returned in the
     * response. If the given routes contain "Red", use a time limit of 30 to allow both Ashmont
     * and Braintree schedules for a single direction. No other routes should have this problem.
     * @param routes A list of MBTA route names.
     * @return An optional Routes object.
     */
    public static Optional<Routes> fetchSchedulesByRoutes(List<String> routes) {
        String queryString = "routes=" + String.join(",", routes);
        queryString += routes.contains("Red") ? "&max_time=30" : "&max_trips=1";
        return fetch("scheduleByRoutes", queryString, Routes.class);
    }
}