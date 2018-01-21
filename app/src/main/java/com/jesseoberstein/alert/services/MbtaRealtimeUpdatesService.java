package com.jesseoberstein.alert.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.providers.EndpointsProvider;
import com.jesseoberstein.alert.providers.StationsProvider;
import com.jesseoberstein.mbta.model.Route;
import com.jesseoberstein.mbta.model.Routes;
import com.jesseoberstein.mbta.model.Stop;
import com.jesseoberstein.mbta.utils.UrlBuilder;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.jesseoberstein.alert.utils.AlertUtils.getObjectFromIntent;
import static com.jesseoberstein.alert.utils.Constants.ALARM_ID;
import static com.jesseoberstein.alert.utils.Constants.COLOR;
import static com.jesseoberstein.alert.utils.Constants.ENDPOINTS;
import static com.jesseoberstein.alert.utils.Constants.NICKNAME;
import static com.jesseoberstein.alert.utils.Constants.ROUTE;
import static com.jesseoberstein.alert.utils.Constants.STOP_IDS;
import static com.jesseoberstein.alert.utils.DateUtils.formatUnixTimestamp;
import static com.jesseoberstein.mbta.utils.ResponseParser.parse;

public class MbtaRealtimeUpdatesService extends IntentService {
    private static NotificationManager notificationManager;
    private static StationsProvider stationsProvider;
    private static EndpointsProvider endpointsProvider;

    public MbtaRealtimeUpdatesService() {
        super("MbtaRealtimeUpdatesService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (stationsProvider == null) {
            stationsProvider = StationsProvider.init(getAssets());
        }
        if (endpointsProvider == null) {
            endpointsProvider = EndpointsProvider.init(getAssets());
        }
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        }

        List<String> endpoints = Arrays.asList((String[]) getObjectFromIntent(intent, ENDPOINTS).orElse(new String[]{}));
        String route = (String) getObjectFromIntent(intent, ROUTE).orElse("");
        List<String> routeNames = endpointsProvider.getRoutesFromEndpoints(endpoints, route).stream()
                .map(Route::getRouteId)
                .collect(Collectors.toList());

        if (routeNames.isEmpty()) {
            System.out.println("Did not make any requests, no routes found.");
        }

        System.out.println(routeNames);
        Optional.ofNullable(UrlBuilder.urlBuilder()
                .withEndpoint("predictionsbyroutes")
                .withQuery("routes=" + TextUtils.join(",", routeNames)).build())
                .map(URL::toString)
                .ifPresent(url -> {
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    queue.add(new StringRequest(
                            Request.Method.GET, url,
                            response -> handleSuccess(response, endpoints, intent),
                            error -> handleError(error, url, intent)));
                });
    }

    private void handleSuccess(String response, List<String> endpoints, Intent intent) {
        List<String> stopIds = Arrays.asList(getObjectFromIntent(intent, STOP_IDS).map(obj -> ((String[]) obj)).orElse(new String[]{}));
        Map<String, Stop> arrivalsByTrip = new HashMap<>();

        parse(response, Routes.class).ifPresent(routes -> routes.getModes().stream()
                .flatMap(mode -> mode.getRoutes().stream())
                .flatMap(route -> route.getDirections().stream())
                .flatMap(direction -> direction.getTrips().stream())
                .filter(trip -> endpoints.contains(trip.getEndpoint()) &&
                                !trip.getStops().stream()
                                        .filter(stop -> stopIds.contains(stop.getStopId()))
                                        .collect(Collectors.toList())
                                        .isEmpty())
                .forEach(trip -> arrivalsByTrip.put(trip.getTripId(), trip.getStops().stream()
                        .filter(stop -> stopIds.contains(stop.getStopId()))
                        .findFirst().orElse(new Stop()))));

        Map<String, Long> arrivalsTimesMap = new HashMap<>();
        arrivalsByTrip.forEach((tripId, stop) -> arrivalsTimesMap.put(tripId, stop.getPredictedArrivalTime()));
        List<String> arrivals = arrivalsTimesMap.entrySet().stream()
                .filter(entry -> entry.getValue() != 0)
                .sorted(Map.Entry.comparingByValue())
                .limit(2)
                .map(entry -> formatUnixTimestamp(getApplicationContext(), entry.getValue()))
                .collect(Collectors.toList());

        String stopId = arrivalsByTrip.entrySet().stream().map(entry -> entry.getValue().getStopId()).findFirst().orElse("");
        String stopName = stationsProvider.getStopById(stopId).map(Stop::getRealStopName).orElse("");
        String stopAndEndpoint = endpoints.isEmpty() ? "" : String.format("%s \u2022 %s", stopName, endpoints.get(0));
        String msg = arrivals.isEmpty() ?
            String.format("%s\nNo trains arriving soon.", stopAndEndpoint) :
            (arrivals.size() == 1) ?
                String.format("%s\nNext train arriving at %s.", stopAndEndpoint, arrivals.get(0)) :
                String.format("%s\nNext trains arriving at %s and %s.", stopAndEndpoint, arrivals.get(0), arrivals.get(1));

        int alarmId = (int) getObjectFromIntent(intent, ALARM_ID).orElse(-1);
        if (alarmId > -1) {
            String alarmNickname = (String) getObjectFromIntent(intent, NICKNAME).orElse("Your Alarm");
            int routeColor = getObjectFromIntent(intent, COLOR).map(id -> getColor(((int) id))).orElse(Color.BLACK);
            pushNotification(alarmId, alarmNickname, msg.split("\n")[1], msg.split("\n")[0], routeColor);
        } else {
            System.out.println("The notification id could not be created for this alarm. Id = " + alarmId);
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }
    }

    private void handleError(VolleyError error, String url, Intent intent) {
        System.out.println(String.format("Could not complete request with url: %s.\nError: %s", url, error.getMessage()));
        int alarmId = (int) getObjectFromIntent(intent, ALARM_ID).orElse(-1);
        String errorTitle = error.getMessage(); //"Error fetching MBTA arrival times.";
        String errorPrimary = "Please check your internet connection.";

        if (alarmId > -1) {
            pushNotification(alarmId, errorTitle, errorPrimary, "", Color.RED);
        }
        else {
            System.out.println("The notification id could not be created for this alarm. Id = " + alarmId);
            Toast.makeText(getApplicationContext(), errorTitle + " " + errorPrimary, Toast.LENGTH_LONG).show();
        }
    }

    private void pushNotification(int notificationId, String title, String primaryText, String subText, int color) {
        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_train)
                .setContentTitle(title)
                .setContentText(primaryText)
                .setSubText(subText)
                .setColor(color)
                .setLocalOnly(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setLights(color, 300, 1000)
                .build();

        notificationManager.notify(notificationId, notification);
    }
}
