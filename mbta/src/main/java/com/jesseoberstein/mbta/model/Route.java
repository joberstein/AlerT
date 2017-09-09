package com.jesseoberstein.mbta.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jesseoberstein.mbta.utils.RouteName;

import java.util.Collections;
import java.util.List;

public class Route {
    @JsonProperty("route_id")
    private String routeId;

    @JsonProperty("route_name")
    private String routeName;

    @JsonProperty("route_hide")
    private boolean routeHide;

    @JsonProperty("parent_route")
    private String parentRoute;

    @JsonProperty("direction")
    private List<Direction> directions;

    public Route() {}

    public Route(String routeName) {
        setRouteName(routeName);
        this.routeId = routeName;
        this.routeHide = false;
        this.directions = Collections.emptyList();
    }

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

    public boolean isRouteHide() {
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

    public void setParentRoute(String parentRoute) {
        this.parentRoute = parentRoute;
    }

    public List<Direction> getDirections() {
        return directions;
    }

    public void setDirections(List<Direction> directions) {
        this.directions = directions;
    }

    @Override
    public String toString() {
        return "\n\t\t\t\t" +
                "{" + "\n\t\t\t\t\t" +
                "routeId: " + routeId + "\n\t\t\t\t\t" +
                "routeName: " + routeName + "\n\t\t\t\t\t" +
                "routeHide: " + routeHide + "\n\t\t\t\t" +
                "directions: " + directions + "\n\t\t\t\t" +
                "}";
    }
}