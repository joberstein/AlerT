package com.jesseoberstein.mbta;

import com.jesseoberstein.mbta.model.Directions;
import com.jesseoberstein.mbta.model.Route;
import com.jesseoberstein.mbta.model.Routes;
import com.jesseoberstein.mbta.model.Stop;
import com.jesseoberstein.mbta.utils.ResponseParser;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.jesseoberstein.mbta.api.Endpoints.fetchRoutes;
import static com.jesseoberstein.mbta.api.Endpoints.fetchStopsByRoute;

public class FetchMbtaData {

    public static void main(String[] args) {
        List<Optional<Directions>> optDirections = getAllRouteDirections();
        optDirections.forEach(
                optDirection -> optDirection.ifPresent(
                        directions -> directions.getDirections().forEach(
                                direction -> setEndpointsForRoute(direction.getStops()))));

        List<Directions> directions = optDirections.stream()
                .map(opt -> opt.orElse(null))
                .collect(Collectors.toList());

        ResponseParser.write(directions, "stopsByRoute");
    }

    private static void setEndpointsForRoute(List<Stop> routeStops) {
        if (!routeStops.isEmpty()) {
            routeStops.get(0).setIsEndpoint(true);
        }
        if (routeStops.size() > 1) {
            routeStops.get(routeStops.size() - 1).setIsEndpoint(true);
        }
    }

    private static List<Optional<Directions>> getAllRouteDirections() {
        return getRoutes().stream()
                .map(FetchMbtaData::getRouteDirections)
                .collect(Collectors.toList());
    }

    private static List<Route> getRoutes() {
        Optional<Routes> optRoutes = fetchRoutes();
        optRoutes.ifPresent(routes -> ResponseParser.write(routes, "routes"));
        return optRoutes
                .map(routes -> routes.getModes().stream()
                        .flatMap(mode -> mode.getRoutes().stream())
                        .collect(Collectors.toList()))
                .orElseGet(Collections::emptyList);
    }

    private static Optional<Directions> getRouteDirections(Route route) {
        Optional<Directions> optDirections = fetchStopsByRoute(route.getRouteId());
        optDirections.ifPresent(directions -> directions.setRoute(route));
        return optDirections;
    }
}
