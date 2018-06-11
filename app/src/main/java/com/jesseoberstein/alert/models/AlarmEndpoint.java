package com.jesseoberstein.alert.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.jesseoberstein.alert.models.mbta.Endpoint;

@Entity(
    tableName = "alarm_endpoints",
    indices = {@Index("alarm_id"), @Index("endpoint_id")}
)
public class AlarmEndpoint {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "alarm_id")
    @ForeignKey(entity = UserAlarm.class, parentColumns = "id", childColumns = "alarm_id")
    private long alarmId;

    @ColumnInfo(name = "endpoint_id")
    @ForeignKey(entity = Endpoint.class, parentColumns = "id", childColumns = "endpoint_id")
    private long endpointId;

    public AlarmEndpoint(long alarmId, long endpointId) {
        this.alarmId = alarmId;
        this.endpointId = endpointId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(long alarmId) {
        this.alarmId = alarmId;
    }

    public long getEndpointId() {
        return endpointId;
    }

    public void setEndpointId(long endpointId) {
        this.endpointId = endpointId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlarmEndpoint that = (AlarmEndpoint) o;

        if (id != that.id) return false;
        if (alarmId != that.alarmId) return false;
        return endpointId == that.endpointId;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (alarmId ^ (alarmId >>> 32));
        result = 31 * result + (int) (endpointId ^ (endpointId >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "AlarmEndpoint{" +
                "id=" + id +
                ", alarmId=" + alarmId +
                ", endpointId=" + endpointId +
                '}';
    }
}
