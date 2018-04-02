package com.jesseoberstein.alert.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.jesseoberstein.alert.BR;
import com.jesseoberstein.mbta.model.Route;
import com.jesseoberstein.mbta.model.RouteType;

import java.io.Serializable;
import java.util.Optional;

import static com.jesseoberstein.alert.utils.AlertUtils.getRouteResource;
import static com.jesseoberstein.alert.utils.Constants.COLOR;
import static com.jesseoberstein.alert.utils.Constants.ICON;
import static com.jesseoberstein.alert.utils.Constants.THEME;

@DatabaseTable(tableName = "user_routes")
public class UserRoute extends BaseObservable implements Serializable {

    @DatabaseField(columnName = "route_id", id = true)
    private String routeId;

    @DatabaseField(columnName = "route_name")
    private String routeName;

    @DatabaseField(columnName = "route_type")
    private RouteType routeType;

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

    public UserRoute(Route route) {
        this.routeId = route.getId();
        this.routeName = route.toString();
        this.routeType = route.getRouteType();
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

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    @Bindable
    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
        notifyPropertyChanged(BR.routeName);
    }

    public RouteType getRouteType() {
        return routeType;
    }

    public void setRouteType(RouteType routeType) {
        this.routeType = routeType;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserRoute userRoute = (UserRoute) o;

        if (icon != userRoute.icon) return false;
        if (theme != userRoute.theme) return false;
        if (color != userRoute.color) return false;
        if (routeId != null ? !routeId.equals(userRoute.routeId) : userRoute.routeId != null)
            return false;
        if (routeName != null ? !routeName.equals(userRoute.routeName) : userRoute.routeName != null)
            return false;
        return alerts != null ? alerts.equals(userRoute.alerts) : userRoute.alerts == null;
    }

    @Override
    public int hashCode() {
        int result = routeId != null ? routeId.hashCode() : 0;
        result = 31 * result + (routeName != null ? routeName.hashCode() : 0);
        result = 31 * result + (alerts != null ? alerts.hashCode() : 0);
        result = 31 * result + icon;
        result = 31 * result + theme;
        result = 31 * result + color;
        return result;
    }
}
