package com.jesseoberstein.alert.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jesseoberstein.alert.utils.AlertUtils;
import com.jesseoberstein.mbta.model.Stop;
import com.jesseoberstein.mbta.model.Trip;
import com.jesseoberstein.mbta.utils.ResponseParser;
import com.jesseoberstein.mbta.utils.UrlBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.jesseoberstein.alert.utils.Constants.ENDPOINTS;
import static com.jesseoberstein.alert.utils.Constants.STOP_IDS;
import static com.jesseoberstein.alert.utils.DateUtils.formatUnixTimestamp;
import static com.jesseoberstein.alert.utils.DateUtils.isUnixTimestampAfterCurrentTime;

public class MbtaRealtimeUpdatesService extends IntentService {
    private static Map<String, Trip> arrivalsByTrip = new HashMap<>();

    public MbtaRealtimeUpdatesService() {
        super("MbtaRealtimeUpdatesService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        List<String> endpoints = AlertUtils.getStringListFromIntent(intent, ENDPOINTS);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        AlertUtils.getStringListFromIntent(intent, STOP_IDS).stream()
                .map(stop -> UrlBuilder.urlBuilder().withEndpoint("predictionsbystop").withQuery("stop=" + stop).build())
                .filter(Objects::nonNull)
                .map(url -> new StringRequest(
                        Request.Method.GET, url.toString(),
                        response -> handleSuccess(response, endpoints),
                        this::handleError))
                .forEach(queue::add);
    }

    private void handleSuccess(String response, List<String> endpoints) {
        ResponseParser.parse(response, Stop.class).ifPresent(stop ->
                stop.getModes().stream()
                        .flatMap(mode -> mode.getRoutes().stream())
                        .flatMap(route -> route.getDirections().stream())
                        .flatMap(direction -> direction.getTrips().stream())
                        .filter(trip -> endpoints.contains(trip.getEndpoint()))
                        .forEach(trip -> arrivalsByTrip.put(trip.getTripId(), trip)));

        arrivalsByTrip = arrivalsByTrip.entrySet().stream()
                .filter(entry -> isUnixTimestampAfterCurrentTime(entry.getValue().getPredictedDepartureTime()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<String, String> formattedArrivals = arrivalsByTrip.entrySet().stream().collect(
                Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> formatUnixTimestamp(getApplicationContext(), entry.getValue().getPredictedDepartureTime())));

        System.out.println(formattedArrivals);
    }

    private void handleError(VolleyError error) {
        System.out.println("Could not complete request: " + error);
    }
}
