package com.jesseoberstein.alert.models.mbta;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jasminb.jsonapi.annotations.Relationship;
import com.github.jasminb.jsonapi.annotations.Type;

@Type("trip")
public class Trip extends BaseResource {

    @JsonProperty("name")
    private String name;

    @JsonProperty("wheelchair_accessible")
    private int wheelchairAccessible;

    @JsonProperty("bikes_allowed")
    private int bikesAllowed;

    @JsonProperty("headsign")
    private String headsign;

    @JsonProperty("direction_id")
    private int directionId;

    @JsonProperty("block_id")
    private String blockId;

    @Relationship("route")
    private Route route;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWheelchairAccessible() {
        return wheelchairAccessible;
    }

    public void setWheelchairAccessible(int wheelchairAccessible) {
        this.wheelchairAccessible = wheelchairAccessible;
    }

    public int getBikesAllowed() {
        return bikesAllowed;
    }

    public void setBikesAllowed(int bikesAllowed) {
        this.bikesAllowed = bikesAllowed;
    }

    public String getHeadsign() {
        return headsign;
    }

    public void setHeadsign(String headsign) {
        this.headsign = headsign;
    }

    public int getDirectionId() {
        return directionId;
    }

    public void setDirectionId(int directionId) {
        this.directionId = directionId;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public Route getRoute() {
        return this.route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    @Override
    public String toString() {
        return "\n" +
                "Trip {" +
                super.toString() +
                "name='" + name + '\'' +
                ", wheelchairAccessible=" + wheelchairAccessible +
                ", bikesAllowed=" + bikesAllowed +
                ", headsign='" + headsign + '\'' +
                ", directionId=" + directionId +
                ", blockId='" + blockId + '\'' +
                '}';
    }
}
