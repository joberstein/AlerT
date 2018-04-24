package com.jesseoberstein.mbta.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jasminb.jsonapi.annotations.Type;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Type("stop")
public class Stop extends BaseResource implements MbtaDataType {

    @JsonProperty("name")
    private String name;

    @JsonProperty("wheelchair_boarding")
    private int wheelchairBoarding;

    @JsonProperty("longitude")
    private double longitude;

    @JsonProperty("latitude")
    private double latitude;

    private Set<String> routeIds = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWheelchairBoarding() {
        return wheelchairBoarding;
    }

    public void setWheelchairBoarding(int wheelchairBoarding) {
        this.wheelchairBoarding = wheelchairBoarding;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Set<String> getRouteIds() {
        return this.routeIds;
    }

    public void addRouteId(String routeId) {
        this.routeIds.add(routeId);
    }

    public String getValues() {
        List<String> values = Arrays.asList(
                getId(),
                this.name,
                Double.toString(this.latitude),
                Double.toString(this.longitude),
                Integer.toString(this.wheelchairBoarding),
                this.routeIds.toString());

        return String.join("|", values);
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stop stop = (Stop) o;

        if (wheelchairBoarding != stop.wheelchairBoarding) return false;
        if (Double.compare(stop.longitude, longitude) != 0) return false;
        if (Double.compare(stop.latitude, latitude) != 0) return false;
        return name != null ? name.equals(stop.name) : stop.name == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name != null ? name.hashCode() : 0;
        result = 31 * result + wheelchairBoarding;
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
