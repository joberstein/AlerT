package com.jesseoberstein.alert.models.mbta;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jasminb.jsonapi.annotations.Type;

@Entity(tableName = "stops", indices = {@Index("route_id")}, primaryKeys = {"id", "route_id"})
@Type("stop")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Stop extends BaseResource implements Parcelable {

    @JsonProperty("name")
    private String name;

    @ColumnInfo(name = "wheelchair_boarding")
    @JsonProperty("wheelchair_boarding")
    private int wheelchairBoarding;

    @JsonProperty("longitude")
    private double longitude;

    @JsonProperty("latitude")
    private double latitude;

    @ColumnInfo(name = "route_id")
    @ForeignKey(entity = Route.class, parentColumns = "id", childColumns = "route_id")
    @JsonProperty("route_id")
    @NonNull
    private String routeId;

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

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.wheelchairBoarding);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
        dest.writeString(this.routeId);
        dest.writeString(this.getId());
    }

    public Stop() {
    }

    protected Stop(Parcel in) {
        this.name = in.readString();
        this.wheelchairBoarding = in.readInt();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
        this.routeId = in.readString();
        this.setId(in.readString());
    }

    public static final Creator<Stop> CREATOR = new Creator<Stop>() {
        @Override
        public Stop createFromParcel(Parcel source) {
            return new Stop(source);
        }

        @Override
        public Stop[] newArray(int size) {
            return new Stop[size];
        }
    };
}
