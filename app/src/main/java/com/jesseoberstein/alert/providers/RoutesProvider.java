package com.jesseoberstein.alert.providers;

import android.content.res.AssetManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jesseoberstein.mbta.model.Mode;
import com.jesseoberstein.mbta.model.Route;
import com.jesseoberstein.mbta.model.RouteType;
import com.jesseoberstein.mbta.model.Routes;
import com.jesseoberstein.mbta.utils.ModePredicate;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Provides access to MBTA route data in many different forms.
 */
public class RoutesProvider {
    public static final List<Route> RED_LINE_ROUTES = Arrays.asList(new Route("Ashmont"), new Route("Braintree"));
    private static RoutesProvider instance = null;
    private static final ObjectMapper mapper = new ObjectMapper();
    private final AssetManager assetManager;
    private List<Mode> routeModes;
    private List<Route> routes;
    private Map<String, List<Route>> routeModesMap;
    private Map<String, List<Route>> routesMap;

    private RoutesProvider(AssetManager assetManager) {
        this.assetManager = assetManager;
        this.routeModes = this.parseRoutesResponse();
        this.routes = this.getRoutes();
        this.routesMap = this.routes.stream().collect(Collectors.groupingBy(
                Route::getParentRoute,
                HashMap::new,
                Collectors.toCollection(ArrayList::new)
        ));
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
        return this.routeModes.stream()
                .filter(ModePredicate.isModeUsed())
                .flatMap(mode -> mode.getRoutes().stream())
                .filter(route -> !route.isRouteHide())
                .collect(Collectors.toList());
    }

    /**
     * Get the list of route names for the provider's routes (some modes may be
     * filtered out).
     * @return A list of parent route names (different from a route name: Green and
     * Silver Line have multiple branches).
     */
    public List<String> getRouteNames() {
        return this.routes.stream()
                .map(Route::getParentRoute)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Get the list of specific route names for the provider's routes (some modes may be
     * filtered out).
     * @return A list of specific route names (the route name given by the MBTA).
     */
    public List<String> getSpecificRouteNames() {
        return this.routes.stream()
                .map(Route::getRouteName)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * A map of parent route name to a list of routes.  Most entries will only have
     * a value list with one element, but Green and Silver Line Will be different:
     * Ex: "Green Line" = [{"Green Line B" Route}, {"Green Line C" Route}, ...]
     * @return A map of parent route names to lists of routes.
     */
    public Map<String, List<Route>> getRoutesMap() {
        return this.routesMap;
    }

    /**
     * Lookup a route's type/mode based on its name.
     * @param routeName The parent name of a route.
     * @return A route's type, or null if the route name is not valid.
     */
    public RouteType getRouteType(String routeName) {
        Optional<Mode> routeMode = this.routeModes.stream()
                .filter(mode -> !mode.getRoutes().stream()
                        .filter(route -> route.getParentRoute().equals(routeName))
                        .collect(Collectors.toList()).isEmpty())
                .findAny();
        int ordinal = routeMode.map(mode -> mode.getRouteType().type()).orElse(-1);
        RouteType[] routeTypes = RouteType.values();
        return (ordinal >= 0) && (ordinal < routeTypes.length) ? routeTypes[ordinal] : null;
    }

    /**
     * This will parse the "routes_response.json" file into a Routes object and
     * get the modes from it.
     * @return A list of modes, which contains routes.
     */
    private List<Mode> parseRoutesResponse() {
        List<Mode> routes = Collections.emptyList();
        try {
            InputStream routesResponse = this.assetManager.open("routes_response.json");
            routes = mapper.readValue(routesResponse, Routes.class).getModes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return routes;
    }
}
