package com.jesseoberstein.alert.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Optional;

import static com.jesseoberstein.alert.utils.AlertUtils.getRouteResource;
import static com.jesseoberstein.alert.utils.Constants.COLOR;
import static com.jesseoberstein.alert.utils.Constants.ICON;
import static com.jesseoberstein.alert.utils.Constants.THEME;

@DatabaseTable(tableName = "user_routes")
public class UserRoute {

    @DatabaseField(id = true, columnName = "route_name")
    private String routeName;

    @DatabaseField
    private String alerts;

    private int icon;
    private int theme;
    private int color;

    public UserRoute() {}

    public UserRoute(String routeName) {
        this.routeName = routeName;
        this.alerts = "";
        setRouteResources();
    }

    /**
     * Used to populate resources for the above constructor; using setResources here would like to
     * null being used for the resources.
     */
    public UserRoute(String routeName, int color, int icon, int theme) {
        this.routeName = routeName;
        this.color = color;
        this.icon = icon;
        this.theme = theme;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public void setRouteResources() {
        Optional.ofNullable(this.routeName).ifPresent(route -> {
            this.color = getRouteResource(routeName, COLOR);
            this.icon = getRouteResource(routeName, ICON);
            this.theme = getRouteResource(routeName, THEME);
        });
    }

    public String getAlerts() {
        return alerts;
    }

    public void setAlerts(String alerts) {
        this.alerts = alerts;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
