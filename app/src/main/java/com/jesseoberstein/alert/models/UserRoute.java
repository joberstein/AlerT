package com.jesseoberstein.alert.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "user_routes")
public class UserRoute {

    @DatabaseField(id = true)
    private String routeName;

    @DatabaseField
    private int icon;

    @DatabaseField
    private String alerts;

    public UserRoute() {}

    public UserRoute(String routeName, int icon) {
        this.routeName = routeName;
        this.icon = icon;
        this.alerts = "";
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getAlerts() {
        return alerts;
    }

    public void setAlerts(String alerts) {
        this.alerts = alerts;
    }
}
