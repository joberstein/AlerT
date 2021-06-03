package com.jesseoberstein.alert.models.mbta;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jasminb.jsonapi.annotations.Type;

import org.jetbrains.annotations.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@Entity(
        tableName = "stops",
        indices = {@Index("route_id")},
        primaryKeys = {"id", "route_id", "direction_id"},
        foreignKeys = {
            @ForeignKey(entity = Route.class, parentColumns = "id", childColumns = "route_id")
        }
)
@Type("stop")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Stop extends BaseResource {

    @JsonProperty("name")
    private String name;

    @ColumnInfo(name = "wheelchair_boarding")
    @JsonProperty("wheelchair_boarding")
    private int wheelchairBoarding;

    @JsonProperty("longitude")
    private double longitude;

    @JsonProperty("latitude")
    private double latitude;

    @ColumnInfo(name = "route_id")
    @JsonProperty("route_id")
    @NonNull
    private String routeId;

    @ColumnInfo(name = "direction_id")
    @JsonProperty("route_id")
    private int directionId;

    @NotNull
    @Override
    public String toString() {
        return this.name;
    }
}
