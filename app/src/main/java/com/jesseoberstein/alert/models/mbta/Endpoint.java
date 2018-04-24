package com.jesseoberstein.alert.models.mbta;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "endpoints")
public class Endpoint implements Serializable {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    @JsonProperty("name")
    private String name;

    @DatabaseField(columnName = "direction_id")
    @JsonProperty("direction_id")
    private int directionId;

    @DatabaseField(columnName = "direction_name")
    @JsonProperty("direction_name")
    private String directionName;

    @DatabaseField(foreign = true)
    @JsonProperty("route")
    private Route route;

    // Empty constructor for Jackson
    public Endpoint() {}

    public Endpoint(String name, int directionId, String directionName, Route route) {
        this.name = name;
        this.directionId = directionId;
        this.directionName = directionName;
        this.route = route;
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

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
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
        return route != null ? route.equals(endpoint.route) : endpoint.route == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + directionId;
        result = 31 * result + (directionName != null ? directionName.hashCode() : 0);
        result = 31 * result + (route != null ? route.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Endpoint{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", directionId=" + directionId +
                ", directionName='" + directionName + '\'' +
                ", route=" + route +
                '}';
    }
}
