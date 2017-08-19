package com.jesseoberstein.mbta;

import com.jesseoberstein.mbta.model.Directions;
import com.jesseoberstein.mbta.model.Route;
import com.jesseoberstein.mbta.model.Routes;
import com.jesseoberstein.mbta.utils.ResponseParser;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.jesseoberstein.mbta.api.Endpoints.fetchRoutes;
import static com.jesseoberstein.mbta.api.Endpoints.fetchSchedulesByRoutes;
import static com.jesseoberstein.mbta.api.Endpoints.fetchStopsByRoute;

public class FetchMbtaData {

    public static void main(String[] args) {
        List<Route> routes = getRoutes();
        List<Optional<Directions>> optDirections = getAllRouteDirections(routes);
        List<Optional<Routes>> optSchedules = getAllRouteSchedules(routes);

        List<Directions> directions = getElementsFromOptionals(optDirections);
        ResponseParser.write(directions, "stopsByRoute");

        List<Routes> schedules = getElementsFromOptionals(optSchedules);
        ResponseParser.write(schedules, "scheduleByRoutes");
    }

    private static <T> List<T> getElementsFromOptionals(List<Optional<T>> list) {
        return list.stream()
                .map(opt -> opt.orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static List<Optional<Directions>> getAllRouteDirections(List<Route> routes) {
        return routes.stream()
                .map(FetchMbtaData::getRouteDirections)
                .collect(Collectors.toList());
    }

    private static List<Optional<Routes>> getAllRouteSchedules(List<Route> routes) {
        return routes.stream()
                .map(FetchMbtaData::getRouteEndpoints)
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

    private static Optional<Routes> getRouteEndpoints(Route route) {
        List<String> routes = Collections.singletonList(route.getRouteId());
        return fetchSchedulesByRoutes(routes);
    }
}
