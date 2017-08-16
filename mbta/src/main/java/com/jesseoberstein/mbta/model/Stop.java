package com.jesseoberstein.mbta.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Stop {
    @JsonProperty("stop_order")
    private int stopOrder;

    @JsonProperty("stop_id")
    private String stopId;

    @JsonProperty("stop_name")
    private String stopName;

    @JsonProperty("parent_station")
    private String parentStation;

    @JsonProperty("parent_station_name")
    private String parentStationName;

    @JsonProperty("stop_lat")
    private double stopLatitude;

    @JsonProperty("stop_lon")
    private double stopLongitude;

    private boolean isEndpoint;

    public Stop() {
        this.isEndpoint = false;
    }

    public String getRealStopName() {
        return (this.getParentStationName().isEmpty()) ? this.getStopName() : this.getParentStationName();
    }

    public int getStopOrder() {
        return stopOrder;
    }

    public void setStopOrder(int stopOrder) {
        this.stopOrder = stopOrder;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public String getParentStation() {
        return parentStation;
    }

    public void setParentStation(String parentStation) {
        this.parentStation = parentStation;
    }

    public String getParentStationName() {
        return parentStationName;
    }

    public void setParentStationName(String parentStationName) {
        this.parentStationName = parentStationName;
    }

    public double getStopLatitude() {
        return stopLatitude;
    }

    public void setStopLatitude(double stopLatitude) {
        this.stopLatitude = stopLatitude;
    }

    public double getStopLongitude() {
        return stopLongitude;
    }

    public void setStopLongitude(double stopLongitude) {
        this.stopLongitude = stopLongitude;
    }

    public boolean isEndpoint() {
        return this.isEndpoint;
    }

    public void setIsEndpoint(boolean isEndpoint) {
        this.isEndpoint = isEndpoint;
    }

    @Override
    public String toString() {
        return "Stop{" + "\n" +
                "stopOrder=" + stopOrder + "\n" +
                "stopId='" + stopId + "\n" +
                "stopName='" + stopName + "\n" +
                "parentStation='" + parentStation + "\n" +
                "parentStationName='" + parentStationName + "\n" +
                "stopLatitude=" + stopLatitude + "\n" +
                "stopLongitude=" + stopLongitude + "\n" +
                "isEndpoint=" + isEndpoint + "\n" +
                "}\n";
    }
}
