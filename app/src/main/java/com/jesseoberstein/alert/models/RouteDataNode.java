package com.jesseoberstein.alert.models;

public class RouteDataNode {
    private int id;
    private String route;

    public RouteDataNode(int id, String route) {
        this.id = id;
        this.route = route;
    }

    public String getId() {
        return Integer.valueOf(id).toString();
    }

    public String getRoute() {
        return route;
    }
}
