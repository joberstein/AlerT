package com.jesseoberstein.alert.models.mbta;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Entity(tableName = "endpoints", indices = {@Index("route_id")})
public class Endpoint implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @JsonProperty("name")
    private String name;

    @ColumnInfo(name = "direction_id")
    @JsonProperty("direction_id")
    private int directionId;

    @ColumnInfo(name = "direction_name")
    @JsonProperty("direction_name")
    private String directionName;

    @ForeignKey(entity = Route.class, parentColumns = "id", childColumns = "route_id")
    @ColumnInfo(name = "route_id")
    @JsonProperty("routeId")
    private String routeId;

    // Empty constructor for Jackson, but ignore for Room
    @Ignore
    public Endpoint() {}

    public Endpoint(String name, int directionId, String directionName, String routeId) {
        this.name = name;
        this.directionId = directionId;
        this.directionName = directionName;
        this.routeId = routeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Endpoint endpoint = (Endpoint) o;

        if (id != endpoint.id) return false;
        if (directionId != endpoint.directionId) return false;
        if (name != null ? !name.equals(endpoint.name) : endpoint.name != null) return false;
        if (directionName != null ? !directionName.equals(endpoint.directionName) : endpoint.directionName != null)
            return false;
        return routeId != null ? routeId.equals(endpoint.routeId) : endpoint.routeId == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + directionId;
        result = 31 * result + (directionName != null ? directionName.hashCode() : 0);
        result = 31 * result + (routeId != null ? routeId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Endpoint{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", directionId=" + directionId +
                ", directionName='" + directionName + '\'' +
                ", routeId=" + routeId +
                '}';
    }
}
