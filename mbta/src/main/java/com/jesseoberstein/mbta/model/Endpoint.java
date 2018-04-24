package com.jesseoberstein.mbta.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Endpoint implements MbtaDataType {

    @JsonProperty("name")
    private String name;

    @JsonProperty("direction")
    private Direction direction;

    public Endpoint() {}

    public Endpoint(String name, Direction direction) {
        this.name = name;
        this.direction = direction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Endpoint endpoint = (Endpoint) o;

        if (name != null ? !name.equals(endpoint.name) : endpoint.name != null) return false;
        return direction != null ? direction.equals(endpoint.direction) : endpoint.direction == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "\n" +
                "Endpoint {" +
                "name='" + name + '\'' +
                ", direction='" + direction + '\'' +
                '}';
    }
}
