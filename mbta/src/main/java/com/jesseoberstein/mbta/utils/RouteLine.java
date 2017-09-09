package com.jesseoberstein.mbta.utils;

public enum RouteLine {
    A,
    B,
    C,
    D,
    E,
    SL1,
    SL2,
    SL4,
    SL5,
    OTHER;

    public static RouteLine getLineFromRoute(String routeName) {
        switch (routeName) {
            case "Ashmont":     return A;
            case "Braintree":
            case "Green-B":     return B;
            case "Green-C":     return C;
            case "Green-D":     return D;
            case "Green-E":     return E;
            case "741":         return SL1;
            case "742":         return SL2;
            case "751":         return SL4;
            case "749":         return SL5;
            default:            return OTHER;
        }
    }
}
