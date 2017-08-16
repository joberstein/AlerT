package com.jesseoberstein.mbta.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Direction {
    @JsonProperty("direction_id")
    private int directionId;

    @JsonProperty("direction_name")
    private String directionName;

    @JsonProperty("stop")
    private List<Stop> stops;

    public Direction() {}

    public int getDirectionId() {
        return directionId;
    }

    public void setDirectionId(int directionId) {
        this.directionId = directionId;
    }

    public String getDirectionName() {
        return directionName;
    }

    public void setDirectionName(String directionName) {
        this.directionName = directionName;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    @Override
    public String toString() {
        return "Direction{" + "\n" +
                "directionId=" + directionId + "\n" +
                "directionName='" + directionName + "\n" +
                "stops=" + stops + "\n" +
                "}\n";
    }
}
