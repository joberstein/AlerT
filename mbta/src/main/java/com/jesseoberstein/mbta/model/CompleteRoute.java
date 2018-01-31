package com.jesseoberstein.mbta.model;

import java.util.List;

public class CompleteRoute {

    private Route route;
    private List<Stop> stops;
    private List<Endpoint> endpoints;

    public CompleteRoute() {}

    public CompleteRoute(Route route, List<Stop> stops, List<Endpoint> endpoints) {
        this.route = route;
        this.stops = stops;
        this.endpoints = endpoints;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    public List<Endpoint> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<Endpoint> endpoints) {
        this.endpoints = endpoints;
    }

    @Override
    public String toString() {
        return "\n" +
                "CompleteRoute {" +
                "route=" + route +
                ", stops=" + stops +
                ", endpoints=" + endpoints +
                '}';
    }
}
