package com.jesseoberstein.alert.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.jesseoberstein.alert.BR;
import com.jesseoberstein.mbta.model.Stop;

import java.io.Serializable;

@DatabaseTable(tableName = "user_stops")
public class UserStop extends BaseObservable implements Serializable {

    @DatabaseField(columnName = "stop_id", id = true)
    private String stopId;

    @DatabaseField(columnName = "stop_name")
    private String stopName;

    public UserStop() {}

    public UserStop(Stop stop) {
        this.stopId = stop.getId();
        this.stopName = stop.toString();
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    @Bindable
    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
        notifyPropertyChanged(BR.stopName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserStop userStop = (UserStop) o;

        if (stopId != null ? !stopId.equals(userStop.stopId) : userStop.stopId != null)
            return false;
        return stopName != null ? stopName.equals(userStop.stopName) : userStop.stopName == null;
    }

    @Override
    public int hashCode() {
        int result = stopId != null ? stopId.hashCode() : 0;
        result = 31 * result + (stopName != null ? stopName.hashCode() : 0);
        return result;
    }
}
