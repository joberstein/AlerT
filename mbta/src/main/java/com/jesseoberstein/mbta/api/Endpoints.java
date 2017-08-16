package com.jesseoberstein.mbta.api;

import com.jesseoberstein.mbta.model.Directions;
import com.jesseoberstein.mbta.model.Routes;

import java.util.Optional;

import static com.jesseoberstein.mbta.api.Fetch.*;

public class Endpoints {

    public static Optional<Routes> fetchRoutes() {
        return fetch("routes", "", Routes.class);
    }

    public static Optional<Directions> fetchStopsByRoute(String route) {
        return fetch("stopsByRoute", "route=" + route, Directions.class);
    }
}