package com.jesseoberstein.mbta.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Endpoint implements MbtaDataType {

    @JsonProperty("name")
    private String name;

    @JsonProperty("route_id")
    private String routeId;

    @JsonProperty("direction_id")
    private int directionId;

    @JsonProperty("direction_name")
    private String directionName;

    public Endpoint() {}

    public Endpoint(String name, String routeId, int directionId, String directionName) {
        this.name = name;
        this.routeId = routeId;
        this.directionId = directionId;
        this.directionName = directionName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Endpoint endpoint = (Endpoint) o;
        return name.equals(endpoint.name) && directionId == endpoint.directionId;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + directionId;
        return result;
    }

    @Override
    public String toString() {
        return "\n" +
                "Endpoint {" +
                "name='" + name + '\'' +
                ", routeId='" + routeId + '\'' +
                ", directionId=" + directionId +
                ", directionName=" + directionName +
                '}';
    }
}
