package com.jesseoberstein.mbta.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Mode {
    @JsonProperty("route_type")
    private RouteType routeType;

    @JsonProperty("mode_name")
    private String modeName;

    @JsonProperty("route")
    private List<Route> routes;

    public Mode() {}

    public Mode(RouteType routeType, String modeName, List<Route> routes) {
        this.routeType = routeType;
        this.modeName = modeName;
        this.routes = routes;
    }

    public RouteType getRouteType() {
        return routeType;
    }

    public void setRouteType(RouteType routeType) {
        this.routeType = routeType;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    @Override
    public String toString() {
        return "\n\t\t" +
                "{" + "\n\t\t\t" +
                "routeType: " + routeType.type() + "\n\t\t\t" +
                "modeName: " + modeName + "\n\t\t\t" +
                "routes: " + routes + "\n\t\t" +
                "}";
    }
}
