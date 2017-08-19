package com.jesseoberstein.mbta.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Directions {
    @JsonProperty("direction")
    List<Direction> directions;

    @JsonProperty("route")
    Route route;

    public Directions() {
        this.route = null;
    }

    public List<Direction> getDirections() {
        return directions;
    }

    public void setDirections(List<Direction> directions) {
        this.directions = directions;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    @Override
    public String toString() {
        return "\n" +
                "{" + "\n\t" +
                "directions: " + directions + "\n" +
                "routeName: " + route + "\n" +
                "}";
    }
}
