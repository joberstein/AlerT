package com.jesseoberstein.alert.providers;

import android.content.res.AssetManager;

import com.jesseoberstein.mbta.model.CompleteRoute;
import com.jesseoberstein.mbta.model.Route;
import com.jesseoberstein.mbta.utils.ResponseParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.jesseoberstein.alert.utils.Constants.MBTA_DATA_DATE;

/**
 * Provides access to MBTA route data in many different forms.
 */
public class RoutesProvider {

    private static RoutesProvider instance = null;
    private static List<Route> routes;

    private RoutesProvider(AssetManager assetManager) {
        serializeRoutes(assetManager);
    }

    /**
     * Initialize the RoutesProvider if it doesn't already exist.
     * @param assetManager The android's context asset manager.
     * @return RoutesProvider
     */
    public static RoutesProvider init(AssetManager assetManager) {
        if (null == instance) {
            instance = new RoutesProvider(assetManager);
        }
        return instance;
    }

    /**
     * Get MBTA routes.  The list is filtered by mode, since all modes may not be
     * supported right away.
     * @return A list of routes.
     */
    public List<Route> getRoutes() {
        return routes;
    }

    /**
     * Get the list of route names for the provider's routes (some modes may be
     * filtered out).
     * @return A list of parent route names (different from a route name: Green and
     * Silver Line have multiple branches).
     */
    public List<String> getRouteNames() {
        return routes.stream().map(RoutesProvider::getRouteName).collect(Collectors.toList());
    }

    public static String getRouteName(Route route) {
        return route.getShortName().isEmpty() ? route.getLongName() : route.getShortName();
    }

    private void serializeRoutes(AssetManager assetManager) {
        if (routes != null) {
            return;
        }

        try {
            InputStream inputStream = assetManager.open( MBTA_DATA_DATE +"/routes.json");
            routes = ResponseParser.parseJSONApi(inputStream, Route.class);
        } catch (IOException e) {
            e.printStackTrace();
            routes = Collections.emptyList();
        }
    }
}
