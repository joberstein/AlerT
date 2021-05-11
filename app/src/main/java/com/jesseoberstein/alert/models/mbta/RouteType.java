package com.jesseoberstein.alert.models.mbta;

import androidx.annotation.IntDef;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class RouteType implements Serializable {
    private static final int lightRail = 0;
    private static final int subway = 1;
    private static final int commuterRail = 2;
    private static final int bus = 3;
    private static final int boat = 4;

    public static final RouteType LIGHT_RAIL = new RouteType(lightRail);
    public static final RouteType SUBWAY = new RouteType(subway);
    public static final RouteType COMMUTER_RAIL = new RouteType(commuterRail);
    public static final RouteType BUS = new RouteType(bus);
    public static final RouteType BOAT = new RouteType(boat);

    private @RouteTypeEnum int routeType;

    private RouteType(@RouteTypeEnum int routeType) {
        this.routeType = routeType;
    }

    @IntDef({lightRail, subway, commuterRail, bus, boat})
    @Retention(RetentionPolicy.SOURCE)
    private @interface RouteTypeEnum {}

    public int getId() {
        return this.routeType;
    }

    public static RouteType valueOf(int routeTypeId) {
        switch (routeTypeId) {
            case lightRail:
                return LIGHT_RAIL;
            case subway:
                return SUBWAY;
            case commuterRail:
                return COMMUTER_RAIL;
            case bus:
                return BUS;
            case boat:
                return BOAT;
            default:
                return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RouteType routeType1 = (RouteType) o;

        return routeType == routeType1.routeType;
    }

    @Override
    public int hashCode() {
        return routeType;
    }

    @Override
    public String toString() {
        switch (this.routeType) {
            case lightRail:
                return "Light Rail";
            case subway:
                return "Subway";
            case commuterRail:
                return "Commuter Rail";
            case bus:
                return "Bus";
            case boat:
                return "Boat";
            default:
                return "Unknown";
        }
    }
}
