package com.jesseoberstein.alert.models.mbta;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jasminb.jsonapi.annotations.Type;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

@Type("route")
@DatabaseTable(tableName = "routes")
public class Route extends BaseResource {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @DatabaseField
    @JsonProperty("type")
    private RouteType routeType;

    @JsonProperty("sort_order")
    private int sortOrder;

    @DatabaseField(columnName = "short_name")
    @JsonProperty("short_name")
    private String shortName;

    @DatabaseField(columnName = "long_name")
    @JsonProperty("long_name")
    private String longName;

    @JsonProperty("direction_names")
    private List<String> directionNames;

    @DatabaseField
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Route route = (Route) o;

        if (sortOrder != route.sortOrder) return false;
        if (routeType != route.routeType) return false;
        if (shortName != null ? !shortName.equals(route.shortName) : route.shortName != null)
            return false;
        if (longName != null ? !longName.equals(route.longName) : route.longName != null)
            return false;
        if (directionNames != null ? !directionNames.equals(route.directionNames) : route.directionNames != null)
            return false;
        if (description != null ? !description.equals(route.description) : route.description != null)
            return false;
        if (color != null ? !color.equals(route.color) : route.color != null) return false;
        return textColor != null ? textColor.equals(route.textColor) : route.textColor == null;
    }

    @Override
    public int hashCode() {
        int result = routeType != null ? routeType.hashCode() : 0;
        result = 31 * result + sortOrder;
        result = 31 * result + (shortName != null ? shortName.hashCode() : 0);
        result = 31 * result + (longName != null ? longName.hashCode() : 0);
        result = 31 * result + (directionNames != null ? directionNames.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (textColor != null ? textColor.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return this.longName.isEmpty() ? this.shortName : this.longName;
    }
}