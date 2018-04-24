package com.jesseoberstein.alert.models.mbta;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jasminb.jsonapi.annotations.Type;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@Type("stop")
@DatabaseTable(tableName = "stops")
public class Stop extends BaseResource {

    @DatabaseField
    @JsonProperty("name")
    private String name;

    @DatabaseField(columnName = "wheelchair_boarding")
    @JsonProperty("wheelchair_boarding")
    private int wheelchairBoarding;

    @DatabaseField
    @JsonProperty("longitude")
    private double longitude;

    @DatabaseField
    @JsonProperty("latitude")
    private double latitude;

    @DatabaseField(foreign = true)
    private Route route;

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

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
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
