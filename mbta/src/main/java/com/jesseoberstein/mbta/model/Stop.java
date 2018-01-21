package com.jesseoberstein.mbta.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

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

    @JsonProperty("pre_dt")
    private long predictedArrivalTime;

    @JsonProperty("pre_away")
    private int predictedSecondsAway;

    @JsonProperty("mode")
    private List<Mode> modes;

    @JsonProperty("alert_headers")
    private List<Alert> alerts;

    public Stop() {
        this.parentStationName = "";
        this.modes = Collections.emptyList();
        this.alerts = Collections.emptyList();
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

    public List<Mode> getModes() {
        return modes;
    }

    public void setModes(List<Mode> modes) {
        this.modes = modes;
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
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
                "predictedArrivalTime=" + predictedArrivalTime + "\n" +
                "predictedSecondsAway=" + predictedSecondsAway + "\n" +
                "modes=" + modes + "\n" +
                "alerts=" + alerts + "\n" +
                "}\n";
    }
}
