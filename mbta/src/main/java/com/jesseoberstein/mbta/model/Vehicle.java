package com.jesseoberstein.mbta.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Vehicle {

    @JsonProperty("vehicle_id")
    private String id;

    @JsonProperty("vehicle_lat")
    private double latitude;

    @JsonProperty("vehicle_lon")
    private double longitude;

    @JsonProperty("vehicle_bearing")
    private int bearing;

    @JsonProperty("vehicle_timestamp")
    private long timestamp;

    @JsonProperty("vehicle_label")
    private String label;

    public Vehicle() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getBearing() {
        return bearing;
    }

    public void setBearing(int bearing) {
        this.bearing = bearing;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id + "\n\t" +
                "latitude=" + latitude + "\n\t" +
                "longitude=" + longitude + "\n\t" +
                "bearing=" + bearing + "\n\t" +
                "timestamp=" + timestamp + "\n\t" +
                "label='" + label + "\n\t" +
                '}';
    }
}
