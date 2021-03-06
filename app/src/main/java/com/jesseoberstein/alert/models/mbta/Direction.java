package com.jesseoberstein.alert.models.mbta;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "directions", indices = {@Index("route_id")}, foreignKeys = {
        @ForeignKey(entity = Route.class, parentColumns = "id", childColumns = "route_id")
})
public class Direction implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;

    @ColumnInfo(name = "direction_id")
    private int directionId;

    @ColumnInfo(name = "route_id")
    @NonNull
    private String routeId;

    public Direction(int directionId, String name, @NonNull String routeId) {
        this.directionId = directionId;
        this.name = name;
        this.routeId = routeId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDirectionId() {
        return directionId;
    }

    public void setDirectionId(int directionId) {
        this.directionId = directionId;
    }

    @NonNull
    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(@NonNull String routeId) {
        this.routeId = routeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Direction direction = (Direction) o;

        if (id != direction.id) return false;
        if (directionId != direction.directionId) return false;
        if (name != null ? !name.equals(direction.name) : direction.name != null) return false;
        return routeId.equals(direction.routeId);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + directionId;
        result = 31 * result + routeId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
