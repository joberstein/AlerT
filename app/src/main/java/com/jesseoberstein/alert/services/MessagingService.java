package com.jesseoberstein.alert.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.models.v2.PredictionNotification;
import com.jesseoberstein.alert.receivers.AlertDismissedReceiver;
import com.jesseoberstein.alert.utils.IdGenerator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static android.app.Notification.CATEGORY_ALARM;
import static android.app.Notification.DEFAULT_ALL;
import static com.jesseoberstein.alert.utils.Constants.CHANNEL_ID;

public class MessagingService extends FirebaseMessagingService {

    private ObjectMapper objectMapper;

    @Override
    public void onCreate() {
        super.onCreate();

        IdGenerator.initialize();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }

    @Override
    public void onNewToken(@NonNull String newToken) {

    }

    @Override
    public void onMessageReceived(RemoteMessage message) {
        PredictionNotification data;

        try {
            String stringifiedData = message.getData().get("data");
            data = objectMapper.readValue(stringifiedData, PredictionNotification.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            Log.e("MessagingService.onMessageReceived", "Could not parse the remote firebase message.");
            return;
        }

        if (data.getDisplayTimes().isEmpty()) {
            return;
        }

        SharedPreferences preferences = this.getSharedPreferences("mbta-alerts", Context.MODE_PRIVATE);

        boolean shouldProcess = getLastReceived(data, preferences)
                .map(lastReceived -> lastReceived.isBefore(data.getCreated()))
                .orElse(true);

        if (!shouldProcess) {
            return;
        }

        String notificationCreated = data.getCreated().format(DateTimeFormatter.ISO_DATE_TIME);

        preferences.edit()
                .putString(getMbtaPredictionsKey(data, "lastReceived"), notificationCreated)
                .apply();

        Set<String> notificationDisplayTimes = data.getDisplayTimes().stream()
                .limit(2)
                .collect(Collectors.toSet());

        boolean isDuplicateNotification = getLastDisplayTimes(data, preferences).equals(notificationDisplayTimes);

        if (isDuplicateNotification) {
            return;
        }

        preferences.edit()
                .putStringSet(getMbtaPredictionsKey(data, "displayTimes"), notificationDisplayTimes)
                .apply();

        int notificationId = IdGenerator.get(data.getRoute(), data.getStop(), data.getDirection());
        NotificationManager nm = this.getSystemService(NotificationManager.class);
        nm.notify(notificationId, buildNotification(notificationId, data));
    }

    private Notification buildNotification(int notificationId, PredictionNotification data) {
        Intent dismissAlertIntent = new Intent(this, AlertDismissedReceiver.class);
        dismissAlertIntent.setAction(Intent.ACTION_DELETE);
        dismissAlertIntent.putExtra("notificationId", Integer.valueOf(notificationId));
        dismissAlertIntent.putExtra("routeId", data.getRoute());
        dismissAlertIntent.putExtra("stopId", data.getStop());

        PendingIntent deleteIntent = PendingIntent.getBroadcast(this, 0, dismissAlertIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action dismissAction = new NotificationCompat.Action.Builder(null, "Dismiss", deleteIntent)
                .setSemanticAction(NotificationCompat.Action.SEMANTIC_ACTION_DELETE)
                .build();

        String title = String.format("%s - %s (%s)", data.getStop(), data.getRoute(), data.getDirection());
        String displayTimeText = data.getDisplayTimes().stream()
                .limit(2)
                .collect(Collectors.joining(" and "));

        String text = "Next trains arriving at: " + displayTimeText;

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_train)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(DEFAULT_ALL)
                .setCategory(CATEGORY_ALARM)
                .addAction(dismissAction)
                .build();
    }

    private String getMbtaPredictionsKey(PredictionNotification data, String namespace) {
        return String.join("/", data.getRoute(), data.getStop(), data.getDirection(), namespace);
    }

    private Optional<LocalDateTime> getLastReceived(PredictionNotification data, SharedPreferences preferences) {
        String lastReceivedKey = getMbtaPredictionsKey(data, "lastReceived");

        return Optional.ofNullable(preferences.getString(lastReceivedKey, null))
                .map(LocalDateTime::parse);
    }

    private Set<String> getLastDisplayTimes(PredictionNotification data, SharedPreferences preferences) {
        String lastDisplayTimesKey = getMbtaPredictionsKey(data, "displayTimes");
        return preferences.getStringSet(lastDisplayTimesKey, Collections.emptySet());
    }
}
