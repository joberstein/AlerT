package com.jesseoberstein.alert.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.models.mbta.Endpoint;
import com.jesseoberstein.alert.models.mbta.Prediction;
import com.jesseoberstein.alert.utils.ResponseParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.jesseoberstein.alert.utils.AlertUtils.getObjectFromIntent;
import static com.jesseoberstein.alert.utils.Constants.ALARM_ID;
import static com.jesseoberstein.alert.utils.Constants.COLOR;
import static com.jesseoberstein.alert.utils.Constants.DIRECTION_ID;
import static com.jesseoberstein.alert.utils.Constants.ENDPOINTS;
import static com.jesseoberstein.alert.utils.Constants.NICKNAME;
import static com.jesseoberstein.alert.utils.Constants.ROUTE;
import static com.jesseoberstein.alert.utils.Constants.STOP_ID;
import static com.jesseoberstein.alert.utils.ResponseParser.formatZonedTime;

public class MbtaRealtimeUpdatesService extends IntentService {
    private static NotificationManager notificationManager;
//    private StopsProvider stopsProvider;
//    private EndpointsProvider endpointsProvider;
    private int alarmId;
    private String alarmNickname;
    private String routeId;
    private int routeColor;
    private String stopId;
    private int directionId;
    private List<String> selectedEndpoints;

    public MbtaRealtimeUpdatesService() {
        super("MbtaRealtimeUpdatesService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        initializeIntentProperties(intent);

        if (notificationManager == null) {
            notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        }

        String queryPattern = "filter[route]=%s&filter[stop]=%s&filter[direction_id]=%d&include=trip";
        String url = new Uri.Builder()
                .scheme("https")
                .authority("api-v3.mbta.com")
                .appendPath("predictions")
                .appendQueryParameter("filter[route]", routeId)
                .appendQueryParameter("filter[stop]", stopId)
                .appendQueryParameter("filter[direction_id]", String.valueOf(directionId))
                .appendQueryParameter("api_key", "0967a76e65da46a2b2c3fcf8c7792efc")
                .build()
                .toString();

        StringRequest request = new StringRequest(Request.Method.GET, url, this::handleSuccess, this::handleError);
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }

    private void handleSuccess(String response) {
        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
        List<Prediction> predictions = ResponseParser.parseJSONApi(inputStream, Prediction.class);
        List<String> arrivalTimes = predictions.stream()
                .filter(prediction -> this.selectedEndpoints.contains(prediction.getTrip().getHeadsign()))
                .limit(2)
                .map(prediction -> formatZonedTime(prediction.getArrivalTime()))
                .collect(Collectors.toList());

       String msg = this.createNotificationMessage(arrivalTimes);

        if (this.alarmId > -1) {
            pushNotification(this.alarmId, this.alarmNickname, msg.split("\n")[1], msg.split("\n")[0], this.routeColor);
        } else {
            System.out.println("The notification id could not be created for this alarm. Id = " + this.alarmId);
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }
    }

    private String createNotificationMessage(List<String> arrivalTimes) {
        String stopName = "";
        List<Endpoint> endpoints = Collections.emptyList(); //endpointsProvider.getEndpointsForDirection(this.directionId);
        List<String> endpointNames = Collections.emptyList(); //endpointsProvider.getEndpointNames(endpoints);
        String stopAndEndpoint = endpoints.isEmpty() ? "" : String.format("%s \u2022 %s", stopName, endpointNames.get(0));

        return arrivalTimes.isEmpty() ?
                String.format("%s\nNo trains arriving soon.", stopAndEndpoint) :
                (arrivalTimes.size() == 1) ?
                        String.format("%s\nNext train arriving at %s.", stopAndEndpoint, arrivalTimes.get(0)) :
                        String.format("%s\nNext trains arriving at %s and %s.", stopAndEndpoint, arrivalTimes.get(0), arrivalTimes.get(1));

    }

    private void handleError(VolleyError error) {
        String url = error.networkResponse.headers.get("Referrer");
        System.out.println(String.format("Could not complete request with url: %s.\nError: %s", url, error.getMessage()));
        String errorTitle = error.getMessage(); //"Error fetching MBTA arrival times.";
        String errorPrimary = "Please check your internet connection.";

        if (alarmId > -1) {
            pushNotification(alarmId, errorTitle, errorPrimary, "", Color.RED);
        }
        else {
            System.out.println("The notification id could not be created for this alarm. Id = " + this.alarmId);
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

    private void initializeIntentProperties(Intent intent) {
        this.routeId = (String) getObjectFromIntent(intent, ROUTE).orElse("");
//        this.stopsProvider = StopsProvider.init(getAssets());
//        this.endpointsProvider = new EndpointsProvider(getAssets(), this.routeId);
        this.alarmId = (int) getObjectFromIntent(intent, ALARM_ID).orElse(-1);
        this.alarmNickname = (String) getObjectFromIntent(intent, NICKNAME).orElse("Your Alarm");
        this.routeColor = getObjectFromIntent(intent, COLOR).map(id -> getColor(((int) id))).orElse(Color.BLACK);
        this.stopId = (String) getObjectFromIntent(intent, STOP_ID).orElse("");
        this.directionId = (int) getObjectFromIntent(intent, DIRECTION_ID).orElse(-1);
        this.selectedEndpoints = Arrays.asList((String []) getObjectFromIntent(intent, ENDPOINTS).orElse(new String[]{}));
    }
}
