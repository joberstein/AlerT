package com.jesseoberstein.mbta.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jesseoberstein.mbta.utils.RouteName;

public class Route {
    @JsonProperty("route_id")
    private String routeId;

    @JsonProperty("route_name")
    private String routeName;

    @JsonProperty("route_hide")
    private boolean routeHide;

    private String parentRoute;

    public Route() {}

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
        setParentRoute();
    }

    public boolean getRouteHide() {
        return routeHide;
    }

    public void setRouteHide(boolean routeHide) {
        this.routeHide = routeHide;
    }

    private void setParentRoute() {
        String normalizedRouteName = this.routeName.toLowerCase().trim();
        if (normalizedRouteName.contains(RouteName.GREEN.toString().toLowerCase())) {
            this.parentRoute = RouteName.GREEN.toString();
        }
        else if (normalizedRouteName.contains(RouteName.SILVER.toString().toLowerCase())) {
            this.parentRoute = RouteName.SILVER.toString();
        }
        else {
            this.parentRoute = this.routeName;
        }
    }

    public String getParentRoute() {
        return this.parentRoute;
    }

    @Override
    public String toString() {
        return "\n\t\t\t\t" +
                "{" + "\n\t\t\t\t\t" +
                "routeId: " + routeId + "\n\t\t\t\t\t" +
                "routeName: " + routeName + "\n\t\t\t\t\t" +
                "routeHide: " + routeHide + "\n\t\t\t\t" +
                "}";
    }
}