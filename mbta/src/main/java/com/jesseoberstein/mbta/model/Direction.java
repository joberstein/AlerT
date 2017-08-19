package com.jesseoberstein.mbta.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public class Direction {
    @JsonProperty("direction_id")
    private int directionId;

    @JsonProperty("direction_name")
    private String directionName;

    @JsonProperty("stop")
    private List<Stop> stops;

    @JsonProperty("trip")
    private List<Trip> trips;

    public Direction() {
        this.stops = Collections.emptyList();
        this.trips = Collections.emptyList();
    }

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

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    @Override
    public String toString() {
        return "{" + "\n\t" +
                "directionId=" + directionId + "\n\t" +
                "directionName='" + directionName + "\n\t" +
                "stops=" + stops + "\n\t" +
                "trips=" + trips + "\n" +
                "}\n";
    }
}
