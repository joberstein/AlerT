package com.jesseoberstein.mbta;

import com.jesseoberstein.mbta.model.Direction;
import com.jesseoberstein.mbta.model.Route;
import com.jesseoberstein.mbta.model.Stop;
import com.jesseoberstein.mbta.utils.ModePredicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.jesseoberstein.mbta.api.Endpoints.fetchRoutes;
import static com.jesseoberstein.mbta.api.Endpoints.fetchStopsByRoute;

public class FetchMbtaData {

    public static void main(String[] args) {
//        getStopsForAllRoutes();
    }

//    private static void printEndpoints(List<List<Stop>> allStops) {
//        allStops.forEach(stops -> {
//            if (!stops.isEmpty()) {
//                stops.get(0).setIsEndpoint(true);
//            }
//            if (stops.size() > 1) {
//                stops.get(stops.size() - 1).setIsEndpoint(true);
//            }
//        });
//
//        List<String> directions = allStops.stream()
//                .flatMap(Collection::stream)
//                .filter(Stop::isEndpoint)
//                .map(Stop::getRealStopName)
//                .distinct()
//                .collect(Collectors.toList());
//
//        directions.forEach(System.out::println);
//    }

    private static List<List<Stop>> getStopsForAllRoutes() {
        return getRoutes().stream()
                .flatMap(route -> getStopsForRoute(route.getRouteId()).stream())
                .collect(Collectors.toList());
    }

    private static List<Route> getRoutes() {
        return fetchRoutes()
                .map(routes -> routes.getModes().stream()
                        .filter(ModePredicate.isModeUsed())
                        .flatMap(mode -> mode.getRoutes().stream())
                        .collect(Collectors.toList()))
                .orElseGet(Collections::emptyList);
    }

    private static List<List<Stop>> getStopsForRoute(String routeName) {
        return fetchStopsByRoute(routeName)
                .map(directions -> directions.getDirections().stream()
                        .map(Direction::getStops)
                        .collect(Collectors.toList()))
                .orElseGet(() -> new ArrayList<>(Collections.emptyList()));
    }
}
