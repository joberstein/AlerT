package com.jesseoberstein.alert.activities;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.data.database.MbtaDatabase;
import com.jesseoberstein.alert.models.mbta.Direction;
import com.jesseoberstein.alert.models.mbta.Endpoint;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.models.mbta.Stop;
import com.jesseoberstein.alert.models.mbta.Trip;
import com.jesseoberstein.alert.network.TaggedRequest;
import com.jesseoberstein.alert.sensitive.SensitiveData;
import com.jesseoberstein.alert.tasks.base.InsertTask;
import com.jesseoberstein.alert.utils.ResponseParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An activity responsible for creating an in-memory database from the data it fetches from the
 * MBTA api.  It creates a database under the name: {@link MbtaDatabase#MBTA_DATABASE_NAME}, which
 * is different from the app's normal database name: {@link AppDatabase#ALERT_DATABASE_NAME}.
 *
 * This activity is currently not meant to be run in production, and it can only be started by
 * modifying the manifest to set this as the launch activity.
 */
public class InitializeMbtaData extends Activity {
    private static final String MBTA_API_SCHEME = "https";
    private static final String MBTA_API_AUTHORITY = "api-v3.mbta.com";
    private RequestQueue requestQueue;
    private MbtaDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestQueue = Volley.newRequestQueue(getApplicationContext());
        this.db = MbtaDatabase.getInstance(getApplicationContext());
        this.fetchRoutes();
    }

    /***********************************************************************************************
     * ROUTES ENDPOINT
     **********************************************************************************************/
    private void fetchRoutes() {
        String url = getUrl("routes", "");
        StringRequest request = new StringRequest(url, this::onReceiveRoutes, e -> this.onReceiveError(e, "routes"));
        this.setRequestOptions(request, "routes");
        this.requestQueue.add(request);
    }

    private void onReceiveRoutes(String response) {
        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
        List<Route> routes = ResponseParser.parseJSONApi(inputStream, Route.class);

        new InsertTask<>(this.db.routeDao()).execute(routes.toArray(new Route[]{}));
        this.fetchStops(routes);
        this.fetchTrips(routes);
    }

    /***********************************************************************************************
     * STOPS ENDPOINT
     **********************************************************************************************/
    private void fetchStops(List<Route> routes) {
        routes.forEach(route -> {
            String url = getUrl("stops", "filter[route]=" + route.getId());
            TaggedRequest request = new TaggedRequest(url, this::onReceiveStops, e -> this.onReceiveError(e, "stops"));
            this.setRequestOptions(request, route);
            this.requestQueue.add(request);
        });
    }

    private void onReceiveStops(Object tag, String response) {
        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
        List<Stop> stops = ResponseParser.parseJSONApi(inputStream, Stop.class);
        stops.forEach(stop -> stop.setRouteId(((Route) tag).getId()));
        new InsertTask<>(this.db.stopDao()).execute(stops.toArray(new Stop[]{}));
    }


    /***********************************************************************************************
     * TRIPS ENDPOINT
     **********************************************************************************************/

    private void fetchTrips(List<Route> routes) {
        routes.forEach(route -> {
            String url = getUrl("trips", "filter[route]=" + route.getId());
            TaggedRequest request = new TaggedRequest(url, this::onReceiveTrips, e -> this.onReceiveError(e, "trips"));
            this.setRequestOptions(request, route);
            this.requestQueue.add(request);
        });
    }

    private void onReceiveTrips(Object tag, String response) {
        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
        Route route = (Route) tag;
        List<Trip> trips = ResponseParser.parseJSONApi(inputStream, Trip.class);
        this.insertEndpoints(trips, route);
        this.insertDirections(trips, route);
    }

    private void insertEndpoints(List<Trip> trips, Route route) {
        List<Endpoint> endpoints = trips.stream()
                .map(trip -> new Endpoint(trip.getHeadsign(), trip.getDirectionId(), route.getId()))
                .distinct()
                .collect(Collectors.toList());

        new InsertTask<>(this.db.endpointDao()).execute(endpoints.toArray(new Endpoint[]{}));
    }

    private void insertDirections(List<Trip> trips, Route route) {
        List<Direction> directions = trips.stream()
                .map(Trip::getDirectionId)
                .map(dirId -> new Direction(dirId, route.getDirectionName(dirId), route.getId()))
                .distinct()
                .collect(Collectors.toList());

        new InsertTask<>(this.db.directionDao()).execute(directions.toArray(new Direction[]{}));
    }


    /***********************************************************************************************
     * COMMON METHODS
     **********************************************************************************************/
    private void onReceiveError(VolleyError error, String entityName) {
        Log.e("mbtaData_onError", "Could not get " + entityName + ". " + error.getMessage());
    }

    private String getUrl(String path, String query) {
        return new Uri.Builder()
                .scheme(MBTA_API_SCHEME)
                .authority(MBTA_API_AUTHORITY)
                .appendPath(path)
                .encodedQuery(query)
                .appendQueryParameter("api_key", SensitiveData.MBTA_API_KEY)
                .build()
                .toString();
    }

    private void setRequestOptions(Request request, Object tag) {
        request.setTag(tag);
        request.setShouldCache(true);
        request.setRetryPolicy(new DefaultRetryPolicy());
    }
}
