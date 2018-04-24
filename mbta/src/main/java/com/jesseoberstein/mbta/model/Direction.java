package com.jesseoberstein.mbta.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Direction implements MbtaDataType {

    @JsonProperty("direction_id")
    private int directionId;

    @JsonProperty("direction_name")
    private String directionName;

    @JsonProperty("route_id")
    private String routeId;

    public Direction() {}

    public Direction(int directionId, String directionName, String routeId) {
        this.directionId = directionId;
        this.directionName = directionName;
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

        Direction direction = (Direction) o;

        if (directionId != direction.directionId) return false;
        if (directionName != null ? !directionName.equals(direction.directionName) : direction.directionName != null)
            return false;
        return routeId != null ? routeId.equals(direction.routeId) : direction.routeId == null;
    }

    @Override
    public int hashCode() {
        int result = directionId;
        result = 31 * result + (directionName != null ? directionName.hashCode() : 0);
        result = 31 * result + (routeId != null ? routeId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\n" +
                "Direction {" +
                ", directionId=" + directionId +
                ", directionName=" + directionName +
                ", routeId=" + routeId +
                '}';
    }
}
