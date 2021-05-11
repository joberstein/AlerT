package com.jesseoberstein.alert.utils;

import android.content.res.Resources;
import android.graphics.Color;
import android.test.mock.MockContext;

import androidx.test.InstrumentationRegistry;

import com.android.volley.Header;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.models.RepeatType;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;
import com.jesseoberstein.alert.models.mbta.Direction;
import com.jesseoberstein.alert.models.mbta.Endpoint;
import com.jesseoberstein.alert.models.mbta.Prediction;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.models.mbta.Stop;
import com.jesseoberstein.alert.models.mbta.Trip;
import com.jesseoberstein.alert.network.UrlBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MbtaRealtimeUpdateHelperTest {

    private FileHelper fileHelper;
    private UserAlarmWithRelations alarmWithRelations;
    private static final Date NOW = new Date();
    private static final long ALARM_ID = 1L;

    @Mock
    private Prediction prediction1;

    @Mock
    private Prediction prediction2;

    @Mock
    private MockContext context;

    @Mock
    private NotificationManagerHelper notificationManagerHelper;

    @Mock
    private UserAlarmScheduler userAlarmScheduler;

    @Mock
    private DateTimeHelper dateTimeHelper;

    @Mock
    private RequestQueue requestQueue;

    @Mock
    private UrlBuilder urlBuilder;

    @InjectMocks
    private MbtaRealtimeUpdatesHelper mbtaRealtimeUpdatesHelper;

    @Before
    public void setup() throws TimeoutException {
        fileHelper = new FileHelper(InstrumentationRegistry.getContext());
        alarmWithRelations = new UserAlarmWithRelations();
        UserAlarm alarm = new UserAlarm();
        alarm.setRepeatType(RepeatType.CUSTOM);
        alarm.setId(ALARM_ID);

        alarmWithRelations.setAlarm(alarm);
        alarmWithRelations.setRoute(mock(Route.class));
        alarmWithRelations.setStop(mock(Stop.class));
        alarmWithRelations.setDirection(mock(Direction.class));

        Resources resources = mock(Resources.class);
        when(context.getResources()).thenReturn(resources);
        when(resources.getString(R.string.alarm_no_predictions)).thenReturn("No predictions");
        when(resources.getString(R.string.error_no_internet)).thenReturn("No internet");
        when(resources.getString(eq(R.string.error_alarm_prediction), anyString())).thenReturn("Prediction error");

        when(alarmWithRelations.getRoute().getId()).thenReturn("routeId");
        when(alarmWithRelations.getRoute().toString()).thenReturn("Route");
        when(alarmWithRelations.getRoute().getColor()).thenReturn("0000FF");
        when(alarmWithRelations.getStop().getId()).thenReturn("stopId");
        when(alarmWithRelations.getStop().getName()).thenReturn("Stop");
        when(alarmWithRelations.getDirection().getDirectionId()).thenReturn(1);

        when(userAlarmScheduler.isTodaySelected(alarm)).thenReturn(true);
        when(userAlarmScheduler.isPastFiringTime(alarm)).thenReturn(false);
        when(urlBuilder.buildPredictionRequestUrl(any(String.class), any(String.class), any(Integer.class))).thenReturn("testRequestUrl");
        when(requestQueue.add(any(StringRequest.class))).thenReturn(null);

        when(prediction1.getArrivalTime()).thenReturn(NOW);
        when(prediction2.getArrivalTime()).thenReturn(Date.from(NOW.toInstant().plusSeconds(60)));
        when(prediction1.getDepartureTime()).thenReturn(NOW);
        when(prediction2.getDepartureTime()).thenReturn(NOW);
        Arrays.asList(prediction1, prediction2).forEach(p -> when(p.getTrip()).thenReturn(mock(Trip.class)));
    }

    @Test
    public void testPredictionRequested_repeatingAlarm() {
        mbtaRealtimeUpdatesHelper.requestPrediction(alarmWithRelations);
        verify(requestQueue, times(1)).add(any(StringRequest.class));
    }

    @Test
    public void testPredictionRequested_singleFireAlarm() {
        alarmWithRelations.getAlarm().setRepeatType(RepeatType.NEVER);
        doReturn(false).when(userAlarmScheduler).isTodaySelected(any(UserAlarm.class));

        mbtaRealtimeUpdatesHelper.requestPrediction(alarmWithRelations);
        verify(requestQueue, times(1)).add(any(StringRequest.class));
    }

    @Test
    public void testPredictionNotRequested_todayNotSelectedForRepeatingAlarm() {
        doReturn(false).when(userAlarmScheduler).isTodaySelected(any(UserAlarm.class));

        mbtaRealtimeUpdatesHelper.requestPrediction(alarmWithRelations);
        verify(requestQueue, never()).add(any(StringRequest.class));
    }

    @Test
    public void testPredictionNotRequested_pastFiringTime() {
        doReturn(true).when(userAlarmScheduler).isPastFiringTime(any(UserAlarm.class));

        mbtaRealtimeUpdatesHelper.requestPrediction(alarmWithRelations);
        verify(requestQueue, never()).add(any(StringRequest.class));
    }

    @Test
    public void testComparePredictionTimes_arrivalTimes() {
        int comparison = mbtaRealtimeUpdatesHelper.compareByPredictionTimes(prediction1, prediction2);
        assertEquals(-1, comparison);
    }

    @Test
    public void testComparePredictionTimes_arrivalTimesNotPresent() {
        Arrays.asList(prediction1, prediction2).forEach(p -> doReturn(null).when(p).getArrivalTime());
        int comparison = mbtaRealtimeUpdatesHelper.compareByPredictionTimes(prediction1, prediction2);
        assertEquals(0, comparison);
    }

    @Test
    public void testComparePredictionTimes_naturalOrderIfNoTimesPresent() {
        Arrays.asList(prediction1, prediction2).forEach(p -> {
            doReturn(null).when(p).getArrivalTime();
            doReturn(null).when(p).getDepartureTime();
        });

        int comparison = mbtaRealtimeUpdatesHelper.compareByPredictionTimes(prediction1, prediction2);
        assertEquals(-1, comparison);
    }

    @Test
    public void testPredictionText() {
        when(prediction1.getTrip().getHeadsign()).thenReturn("Headsign");
        when(dateTimeHelper.getFormattedZonedTime(any(Date.class))).thenReturn("1:07pm");
        String expected = mbtaRealtimeUpdatesHelper.getPredictionText(prediction1);
        assertEquals("1:07pm (Headsign)", expected);
    }

    @Test
    public void testMessageText_noPredictions() {
        List<Prediction> predictions = Collections.emptyList();
        assertEquals("No predictions", mbtaRealtimeUpdatesHelper.getMessageText(predictions));
    }


    @Test
    public void testMessageText_withPredictions() {
        when(prediction1.getTrip().getHeadsign()).thenReturn("Headsign");
        when(prediction2.getTrip().getHeadsign()).thenReturn("Other");
        when(dateTimeHelper.getFormattedZonedTime(any(Date.class))).thenReturn("1:07pm", "12:47am");
        List<Prediction> predictions = Arrays.asList(prediction1, prediction2);
        assertEquals("1:07pm (Headsign)\n12:47am (Other)", mbtaRealtimeUpdatesHelper.getMessageText(predictions));
    }

    @Test
    public void testSuccessfulResponse() throws IOException {
        List<Endpoint> endpoints = Collections.singletonList(new Endpoint("Forest Hills", 1, "Orange"));
        alarmWithRelations.setEndpoints(endpoints);
        when(dateTimeHelper.getFormattedZonedTime(any(Date.class))).thenReturn("1:07pm", "12:47am");

        String message = "1:07pm (Forest Hills)\n12:47am (Forest Hills)";
        List<String> fileLines = fileHelper.readFile("predictions.json");
        mbtaRealtimeUpdatesHelper.requestPrediction(alarmWithRelations);
        mbtaRealtimeUpdatesHelper.handleSuccess(ALARM_ID, fileLines.stream().collect(Collectors.joining("\n")));
        verify(notificationManagerHelper, times(1))
                .pushNotification(1, "Route", "Stop", message, Color.BLUE);
    }

    @Test
    public void testErrorResponse() {
        mbtaRealtimeUpdatesHelper.requestPrediction(alarmWithRelations);
        Header referer = new Header("Referrer", "testUrl");
        NetworkResponse response = new NetworkResponse(500, "".getBytes(), false, 1, Collections.singletonList(referer));
        mbtaRealtimeUpdatesHelper.handleError(ALARM_ID, new VolleyError(response));
        verify(notificationManagerHelper, times(1))
                .pushNotification(1, "Route", "Prediction error", "No internet", Color.RED);
    }
}