package com.jesseoberstein.alert.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.LongSparseArray;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.models.RepeatType;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;
import com.jesseoberstein.alert.models.mbta.Prediction;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.models.mbta.Stop;
import com.jesseoberstein.alert.network.TaggedRequest;
import com.jesseoberstein.alert.network.UrlBuilder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class MbtaRealtimeUpdatesHelper {
    private LongSparseArray<UserAlarmWithRelations> alarmWithRelationsMap;

    private final Context context;
    private final NotificationManagerHelper notificationsHelper;
    private final UserAlarmScheduler userAlarmScheduler;
    private final DateTimeHelper dateTimeHelper;
    private final RequestQueue requestQueue;
    private final UrlBuilder urlBuilder;

    @Inject
    public MbtaRealtimeUpdatesHelper(
            Context context,
            NotificationManagerHelper notificationsHelper,
            UserAlarmScheduler userAlarmScheduler,
            DateTimeHelper dateTimeHelper,
            RequestQueue requestQueue,
            UrlBuilder urlBuilder
    ) {
        this.alarmWithRelationsMap = new LongSparseArray<>();
        this.context = context;
        this.notificationsHelper = notificationsHelper;
        this.userAlarmScheduler = userAlarmScheduler;
        this.dateTimeHelper = dateTimeHelper;
        this.requestQueue = requestQueue;
        this.urlBuilder = urlBuilder;
    }

    public void requestPrediction(UserAlarmWithRelations alarmWithRelations) {
        UserAlarm alarm = alarmWithRelations.getAlarm();
        alarmWithRelationsMap.put(alarm.getId(), alarmWithRelations);

        boolean isAlarmSingleFire = alarm.getRepeatType().equals(RepeatType.NEVER);
        boolean shouldFireAlarmToday = userAlarmScheduler.isTodaySelected(alarm) || isAlarmSingleFire;

        if (shouldFireAlarmToday && !userAlarmScheduler.isPastFiringTime(alarm)) {
            String routeId = alarmWithRelations.getRoute().getId();
            String stopId = alarmWithRelations.getStop().getId();
            int directionId = alarmWithRelations.getDirection().getDirectionId();
            String url = urlBuilder.buildPredictionRequestUrl(routeId, stopId, directionId);

            TaggedRequest request = new TaggedRequest(url, this::handleSuccess, this::handleError);
            request.setTag(alarm.getId());
            requestQueue.add(request);
        }
    }

    void handleSuccess(Object tag, String response) {
        long alarmId = (long) tag;
        UserAlarmWithRelations alarmWithRelations = alarmWithRelationsMap.get(alarmId);
        String[] endpoints = EndpointsUtils.toStringArray(alarmWithRelations.getEndpoints());
        Set<String> endpointNames = new HashSet<>(Arrays.asList(endpoints));
        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
        List<Prediction> predictions = ResponseParser.parseJSONApi(inputStream, Prediction.class)
                .stream()
                .filter(prediction -> endpointNames.contains(prediction.getTrip().getHeadsign()))
                .sorted(this::compareByPredictionTimes)
                .limit(2)
                .collect(toList());

        Route route = alarmWithRelations.getRoute();
        Stop stop = alarmWithRelations.getStop();

        int notificationId = Long.valueOf(alarmId).intValue();
        String messageText = getMessageText(predictions);
        int textColor = Color.parseColor(AlertUtils.getHexColor(route.getColor()));

        notificationsHelper.pushNotification(notificationId, route.toString(), stop.getName(), messageText, textColor);
    }

    void handleError(Object tag, VolleyError error) {
        String url = error.networkResponse.headers.get("Referrer");
        String errorLogMsgFormat = "Could not complete request with url: %s.\nError: %s";
        Log.e(this.getClass().getName(), String.format(errorLogMsgFormat, url, error.getMessage()));

        long alarmId = (long) tag;
        UserAlarmWithRelations alarmWithRelations = alarmWithRelationsMap.get(alarmId);
        Route route = alarmWithRelations.getRoute();
        Stop stop = alarmWithRelations.getStop();

        int notificationId = Long.valueOf(alarmId).intValue();
        String errorTitle = context.getResources().getString(R.string.error_alarm_prediction, stop.getName());
        String errorText = context.getResources().getString(R.string.error_no_internet);

        notificationsHelper.pushNotification(notificationId, route.toString(), errorTitle, errorText, Color.RED);
    }

    int compareByPredictionTimes(Prediction p1, Prediction p2) {
        if (p1.getArrivalTime() != null) {
            return p1.getArrivalTime().compareTo(p2.getArrivalTime());
        }
        if (p1.getDepartureTime() != null) {
            return p1.getDepartureTime().compareTo(p2.getDepartureTime());
        }
        return -1;
    }

    String getMessageText(List<Prediction> predictions) {
        return predictions.isEmpty() ?
                context.getResources().getString(R.string.alarm_no_predictions) :
                predictions.stream().map(this::getPredictionText).collect(joining("\n"));
    }

    String getPredictionText(Prediction prediction) {
        Date predictedArrival = prediction.getArrivalTime();
        Date predictedDeparture = prediction.getDepartureTime();
        Date predictionTime = Optional.ofNullable(predictedArrival).orElse(predictedDeparture);
        String formattedPredictionTime = dateTimeHelper.getFormattedZonedTime(predictionTime);
        String tripHeadsign = prediction.getTrip().getHeadsign();
        return formattedPredictionTime + " (" +  tripHeadsign + ')';
    }
}
