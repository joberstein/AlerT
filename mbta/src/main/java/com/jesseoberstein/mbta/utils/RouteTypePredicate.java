package com.jesseoberstein.mbta.utils;

import com.jesseoberstein.mbta.model.Route;

import java.util.Arrays;
import java.util.function.Predicate;

import static com.jesseoberstein.mbta.model.RouteType.BUS;
import static com.jesseoberstein.mbta.model.RouteType.LIGHT_RAIL;
import static com.jesseoberstein.mbta.model.RouteType.SUBWAY;

public class RouteTypePredicate {

    public static Predicate<Route> isRouteTypeUsed() {
        return route -> Arrays.asList(LIGHT_RAIL, SUBWAY, BUS).contains(route.getRouteType());
    }
}
