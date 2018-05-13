package com.jesseoberstein.alert.models.mbta;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jasminb.jsonapi.annotations.Type;

import java.util.List;

@Entity(tableName = "routes", indices = {@Index("type_id")}, primaryKeys = {"id"})
@Type("route")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Route extends BaseResource {

    @ColumnInfo(name = "type_id")
    @ForeignKey(entity = RouteType.class, parentColumns = "id", childColumns = "type_id")
    @JsonProperty("type")
    private int routeTypeId;

    @ColumnInfo(name = "short_name")
    @JsonProperty("short_name")
    private String shortName;

    @ColumnInfo(name = "long_name")
    @JsonProperty("long_name")
    private String longName;

    @JsonProperty("description")
    private String description;

    @Ignore
    @JsonProperty("sort_order")
    private int sortOrder;

    @Ignore
    @JsonProperty("direction_names")
    private List<String> directionNames;

    @Ignore
    @JsonProperty("color")
    private String color;

    @Ignore
    @JsonProperty("text_color")
    private String textColor;

    @Ignore
    @JsonIgnore
    private RouteType routeType;

    public int getRouteTypeId() {
        return routeTypeId;
    }

    public void setRouteTypeId(int routeTypeId) {
        this.routeTypeId = routeTypeId;
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

    public String getDirectionName(int directionId) {
        boolean isValidDirectionId = directionId >= 0 && directionId < this.directionNames.size();
        return isValidDirectionId ? this.directionNames.get(directionId) : "";
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

    public RouteType getRouteType() {
        return routeType;
    }

    public void setRouteType(RouteType routeType) {
        this.routeType = routeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Route route = (Route) o;

        if (routeTypeId != route.routeTypeId) return false;
        if (sortOrder != route.sortOrder) return false;
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
        int result = routeTypeId;
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