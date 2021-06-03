package com.jesseoberstein.alert.models.mbta;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jasminb.jsonapi.annotations.Type;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@Entity(
        tableName = "routes",
        indices = {@Index("type_id")},
        primaryKeys = {"id"}
)
@Type("route")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Route extends BaseResource {

    @ColumnInfo(name = "type_id")
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

    @JsonProperty("color")
    private String color;

    @ColumnInfo(name = "text_color")
    @JsonProperty("text_color")
    private String textColor;

    @Ignore
    @JsonProperty("sort_order")
    private int sortOrder;

    @Ignore
    @JsonProperty("direction_names")
    private List<String> directionNames;

    @Override
    public String toString() {
        return this.longName.isEmpty() ? this.shortName : this.longName;
    }
}