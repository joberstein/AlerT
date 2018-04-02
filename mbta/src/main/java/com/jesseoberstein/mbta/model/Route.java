package com.jesseoberstein.mbta.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jasminb.jsonapi.annotations.Type;

import java.util.List;

@Type("route")
public class Route extends BaseResource implements MbtaDataType {

    @JsonProperty("type")
    private RouteType routeType;

    @JsonProperty("sort_order")
    private int sortOrder;

    @JsonProperty("short_name")
    private String shortName;

    @JsonProperty("long_name")
    private String longName;

    @JsonProperty("direction_names")
    private List<String> directionNames;

    @JsonProperty("description")
    private String description;

    @JsonProperty("color")
    private String color;

    @JsonProperty("text_color")
    private String textColor;

    public RouteType getRouteType() {
        return routeType;
    }

    public void setRouteType(RouteType routeType) {
        this.routeType = routeType;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public List<String> getDirectionNames() {
        return directionNames;
    }

    public void setDirectionNames(List<String> directionNames) {
        this.directionNames = directionNames;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    @Override
    public String toString() {
        return this.longName.isEmpty() ? this.shortName : this.longName;
    }
}