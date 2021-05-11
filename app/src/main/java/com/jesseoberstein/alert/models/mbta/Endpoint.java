package com.jesseoberstein.alert.models.mbta;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Entity(tableName = "endpoints", indices = {@Index("direction_id"), @Index("route_id")})
public class Endpoint implements Serializable {

    public static final List<String> PSUEDO_ENDPOINT_NAMES = Arrays.asList(
            "JFK/Umass", "Quincy Center", "North Quincy", "Quincy Center");

    @PrimaryKey(autoGenerate = true)
    private long id;

    @JsonProperty("name")
    private String name;

    @ForeignKey(entity = Direction.class, parentColumns = "id", childColumns = "direction_id")
    @ColumnInfo(name = "direction_id")
    private long directionId;

    @ForeignKey(entity = Route.class, parentColumns = "id", childColumns = "route_id")
    @ColumnInfo(name = "route_id")
    private String routeId;

    public Endpoint(String name, long directionId, String routeId) {
        this.name = name;
        this.directionId = directionId;
        this.routeId = routeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDirectionId() {
        return directionId;
    }

    public void setDirectionId(long directionId) {
        this.directionId = directionId;
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
        return routeId != null ? routeId.equals(endpoint.routeId) : endpoint.routeId == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (int) (directionId ^ (directionId >>> 32));
        result = 31 * result + (routeId != null ? routeId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
