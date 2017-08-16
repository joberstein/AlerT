package com.jesseoberstein.mbta.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Directions {
    @JsonProperty("direction")
    List<Direction> directions;

    public Directions() {}

    public List<Direction> getDirections() {
        return directions;
    }

    public void setDirections(List<Direction> directions) {
        this.directions = directions;
    }

    @Override
    public String toString() {
        return "\n" +
                "{" + "\n\t" +
                "directions: " + directions + "\n" +
                "}";
    }
}
