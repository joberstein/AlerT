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

    @JsonProperty("sch_arr_dt")
    private long scheduledArrivalTime;

    @JsonProperty("sch_dep_dt")
    private long scheduleDepartureTime;

    @JsonProperty("pre_dt")
    private long predictedArrivalTime;

    @JsonProperty("pre_away")
    private int predictedSecondsAway;

    @JsonProperty("vehicle")
    private Vehicle vehicle;

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

    public long getScheduledArrivalTime() {
        return scheduledArrivalTime;
    }

    public void setScheduledArrivalTime(long scheduledArrivalTime) {
        this.scheduledArrivalTime = scheduledArrivalTime;
    }

    public long getScheduledDepartureTime() {
        return scheduleDepartureTime;
    }

    public void setScheduledDepartureTime(long scheduleDepartureTime) {
        this.scheduleDepartureTime = scheduleDepartureTime;
    }

    public long getPredictedArrivalTime() {
        return predictedArrivalTime;
    }

    public void setPredictedArrivalTime(long predictedArrivalTime) {
        this.predictedArrivalTime = predictedArrivalTime;
    }

    public int getPredictedSecondsAway() {
        return predictedSecondsAway;
    }

    public void setPredictedSecondsAway(int predictedSecondsAway) {
        this.predictedSecondsAway = predictedSecondsAway;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public String toString() {
        return "{" + "\n\t" +
                "tripId='" + tripId + "\n\t" +
                "tripName='" + tripName + "\n\t" +
                "endpoint='" + endpoint + "\n\t" +
                "stops=" + stops + "\n" +
                "scheduledArrivalTime=" + scheduledArrivalTime + "\n" +
                "scheduledDepartureTime=" + scheduleDepartureTime + "\n" +
                "predictedArrivalTime=" + predictedArrivalTime + "\n" +
                "predictedSecondsAway=" + predictedSecondsAway + "\n" +
                "vehicle=" + vehicle + "\n" +
                "}\n";
    }


}
