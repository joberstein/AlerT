package com.jesseoberstein.alert.models.mbta;

import java.io.Serializable;

public enum RouteType implements Serializable {
    LIGHT_RAIL(0),
    SUBWAY(1),
    COMMUTER_RAIL(2),
    BUS(3),
    BOAT(4);

    private int id;

    RouteType(int id) {
        this.id = id;
    }

    public int type() {
        return this.id;
    }
}
