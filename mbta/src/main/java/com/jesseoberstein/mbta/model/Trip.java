package com.jesseoberstein.mbta.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public class Trip {

    @JsonProperty("trip_id")
    private String tripId;

    @JsonProperty("trip_name")
    private String tripName;

    @JsonProperty("trip_headsign")
    private String endpoint;

    @JsonProperty("stop")
    private List<Stop> stops;

    public Trip() {
        this.stops = Collections.emptyList();
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public List<Stop> getStops() {
        return stops;
    }
    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    @Override
    public String toString() {
        return "{" + "\n\t" +
                "tripId='" + tripId + "\n\t" +
                "tripName='" + tripName + "\n\t" +
                "endpoint='" + endpoint + "\n\t" +
                "stops=" + stops + "\n" +
                "}\n";
    }
}
