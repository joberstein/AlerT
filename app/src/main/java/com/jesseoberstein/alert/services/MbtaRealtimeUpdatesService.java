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
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;
import com.jesseoberstein.alert.models.mbta.Direction;
import com.jesseoberstein.alert.models.mbta.Endpoint;
import com.jesseoberstein.alert.models.mbta.Prediction;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.models.mbta.Stop;
import com.jesseoberstein.alert.sensitive.SensitiveData;
import com.jesseoberstein.alert.utils.AlertUtils;
import com.jesseoberstein.alert.utils.ResponseParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import static com.jesseoberstein.alert.utils.Constants.ALARM;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class MbtaRealtimeUpdatesService extends IntentService {
    private static NotificationManager notificationManager;
    private long alarmId;
    private UserAlarm alarm;
    private Route route;
    private Stop stop;
    private Direction direction;
    private List<Endpoint> endpoints;

    public MbtaRealtimeUpdatesService() {
        super("MbtaRealtimeUpdatesService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        initializeIntentProperties(intent);

        if (notificationManager == null) {
            notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        }

        String url = new Uri.Builder()
                .scheme("https")
                .authority("api-v3.mbta.com")
                .appendPath("predictions")
                .appendQueryParameter("filter[route]", route.getId())
                .appendQueryParameter("filter[stop]", stop.getId())
                .appendQueryParameter("filter[direction_id]", String.valueOf(direction.getDirectionId()))
//                .appendQueryParameter("include", "trip")
                .appendQueryParameter("api_key", SensitiveData.MBTA_API_KEY)
                .build()
                .toString();

        StringRequest request = new StringRequest(Request.Method.GET, url, this::handleSuccess, this::handleError);
        Volley.newRequestQueue(getApplicationContext()).add(request);
    }

    private void handleSuccess(String response) {
        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
        List<Prediction> predictions = ResponseParser.parseJSONApi(inputStream, Prediction.class);
        Set<String> endpointNames = this.endpoints.stream().map(Endpoint::getName).collect(toSet());
        List<String> arrivalTimes = predictions.stream()
                .filter(prediction -> endpointNames.contains(prediction.getTrip().getHeadsign()))
                .limit(2)
                .map(Prediction::getArrivalTime)
                .map(ResponseParser::formatZonedTime)
                .collect(toList());

       String msg = this.createNotificationMessage(arrivalTimes);

        if (alarmId > -1) {
            int notificationId = Long.valueOf(alarmId).intValue();
            String primary = msg.split("\n")[1]; // TODO ?
            String secondary = msg.split("\n")[0];
            int textColor = Color.parseColor(AlertUtils.getTextColorForWhiteBackground(route));
            pushNotification(notificationId, alarm.getNickname(), primary, secondary, textColor);
        } else {
            System.out.println("The notification id could not be created for this alarm. Id = " + alarmId);
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }
    }

    private void handleError(VolleyError error) {
        String url = error.networkResponse.headers.get("Referrer");
        System.out.println(String.format("Could not complete request with url: %s.\nError: %s", url, error.getMessage()));
        String errorTitle = error.getMessage(); //"Error fetching MBTA arrival times.";
        String errorPrimary = "Please check your internet connection.";

        if (alarmId > -1) {
            pushNotification(Long.valueOf(alarmId).intValue(), errorTitle, errorPrimary, "", Color.RED);
        }
        else {
            System.out.println("The notification id could not be created for this alarm. Id = " + alarmId);
            Toast.makeText(getApplicationContext(), errorTitle + " " + errorPrimary, Toast.LENGTH_LONG).show();
        }
    }

    private String createNotificationMessage(List<String> arrivalTimes) {
        // Stop * Endpoint
        String stopAndEndpoint = endpoints.isEmpty() ? "" : String.format("%s \u2022 %s", stop.getName(), endpoints.get(0).getName());

        return arrivalTimes.isEmpty() ?
                String.format("%s\nNo trains arriving soon.", stopAndEndpoint) :
                (arrivalTimes.size() == 1) ?
                        String.format("%s\nNext train arriving at %s.", stopAndEndpoint, arrivalTimes.get(0)) :
                        String.format("%s\nNext trains arriving at %s and %s.", stopAndEndpoint, arrivalTimes.get(0), arrivalTimes.get(1));

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
        UserAlarmWithRelations alarmWithRelations = (UserAlarmWithRelations) intent.getSerializableExtra(ALARM);
        this.alarm = alarmWithRelations.getAlarm();
        this.alarmId = this.alarm.getId();
        this.route = alarmWithRelations.getRoute();
        this.stop = alarmWithRelations.getStop();
        this.direction = alarmWithRelations.getDirection();
        this.endpoints = alarmWithRelations.getEndpoints();
    }
}
