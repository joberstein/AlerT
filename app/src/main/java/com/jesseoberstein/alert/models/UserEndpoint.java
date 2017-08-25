package com.jesseoberstein.alert.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "user_endpoints")
public class UserEndpoint {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(foreign = true)
    private UserAlarm alarm;

    @DatabaseField(columnName = "endpoint_name")
    private String endpointName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserAlarm getAlarm() {
        return alarm;
    }

    public void setAlarm(UserAlarm alarm) {
        this.alarm = alarm;
    }

    public String getEndpointName() {
        return endpointName;
    }

    public void setEndpointName(String endpointName) {
        this.endpointName = endpointName;
    }
}
