package com.jesseoberstein.mbta.utils;

import com.jesseoberstein.mbta.model.RouteType;
import com.jesseoberstein.mbta.model.Mode;

import java.util.function.Predicate;

public class ModePredicate {

    public static Predicate<Mode> isModeUsed() {
        return mode ->
            mode.getRouteType().equals(RouteType.LIGHT_RAIL) ||
            mode.getRouteType().equals(RouteType.SUBWAY) ||
            mode.getRouteType().equals(RouteType.BUS);
    }
}
