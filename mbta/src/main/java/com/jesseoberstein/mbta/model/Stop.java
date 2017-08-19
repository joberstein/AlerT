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

    @JsonProperty("real_stop_name")
    private String realStopName;

    @JsonProperty("stop_sequence")
    private int stopSequence;

    @JsonProperty("sch_arr_dt")
    private long scheduledArrivalTime;

    @JsonProperty("sch_dep_dt")
    private long scheduledDepartureTime;

    public Stop() {
        this.parentStationName = "";
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
        this.realStopName = stopName;
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
        if (!parentStationName.isEmpty()) {
            this.realStopName = parentStationName;
        }
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

    public String getRealStopName() {
        return this.realStopName;
    }

    public void setRealStopName(String realStopName) {
        this.realStopName = realStopName;
    }

    public int getStopSequence() {
        return stopSequence;
    }

    public void setStopSequence(int stopSequence) {
        this.stopSequence = stopSequence;
    }

    public long getScheduledArrivalTime() {
        return scheduledArrivalTime;
    }

    public void setScheduledArrivalTime(long scheduledArrivalTime) {
        this.scheduledArrivalTime = scheduledArrivalTime;
    }

    public long getScheduledDepartureTime() {
        return scheduledDepartureTime;
    }

    public void setScheduledDepartureTime(long scheduledDepartureTime) {
        this.scheduledDepartureTime = scheduledDepartureTime;
    }

    @Override
    public String toString() {
        return "Stop{" + "\n" +
                "stopOrder=" + stopOrder + "\n" +
                "stopId=" + stopId + "\n" +
                "stopName=" + stopName + "\n" +
                "parentStation='" + parentStation + "\n" +
                "parentStationName=" + parentStationName + "\n" +
                "realStopName=" + realStopName + "\n" +
                "stopLatitude=" + stopLatitude + "\n" +
                "stopLongitude=" + stopLongitude + "\n" +
                "stop_sequence=" + stopSequence + "\n" +
                "scheduledArrivalTime=" + scheduledArrivalTime + "\n" +
                "scheduledDepartureTime=" + scheduledDepartureTime + "\n" +
                "}\n";
    }
}
