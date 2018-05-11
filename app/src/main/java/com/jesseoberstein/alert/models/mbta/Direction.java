package com.jesseoberstein.alert.models.mbta;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Entity(tableName = "directions", indices = {@Index("route_id")}, primaryKeys = {"id", "route_id"})
public class Direction implements Serializable, Parcelable {
    private int id;
    private String name;

    @ForeignKey(entity = Route.class, parentColumns = "id", childColumns = "route_id")
    @ColumnInfo(name = "route_id")
    @NonNull
    private String routeId;

    public Direction(int id, String name, @NonNull String routeId) {
        this.id = id;
        this.name = name;
        this.routeId = routeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (name != null ? !name.equals(direction.name) : direction.name != null) return false;
        return routeId.equals(direction.routeId);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + routeId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return this.name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.routeId);
    }

    protected Direction(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.routeId = in.readString();
    }

    public static final Parcelable.Creator<Direction> CREATOR = new Parcelable.Creator<Direction>() {
        @Override
        public Direction createFromParcel(Parcel source) {
            return new Direction(source);
        }

        @Override
        public Direction[] newArray(int size) {
            return new Direction[size];
        }
    };
}
