package com.jesseoberstein.mbta.model;

public enum RouteType implements MbtaDataType {
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
