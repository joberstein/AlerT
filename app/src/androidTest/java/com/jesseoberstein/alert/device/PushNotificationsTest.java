package com.jesseoberstein.alert.device;

import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.activities.alarms.ViewAlarms;
import com.jesseoberstein.alert.activity.editAlarm.AlarmDirectionTest;
import com.jesseoberstein.alert.activity.editAlarm.AlarmEndpointsTest;
import com.jesseoberstein.alert.activity.editAlarm.AlarmRouteTest;
import com.jesseoberstein.alert.activity.editAlarm.AlarmStopTest;
import com.jesseoberstein.alert.activity.editAlarm.AlarmTimeTest;
import com.jesseoberstein.alert.activity.editAlarm.BaseEditAlarmSectionTest;

import junit.framework.Assert;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class PushNotificationsTest {

    private String selectedRoute;
    private String selectedDirection;
    private String selectedStop;
    private String[] selectedEndpoints;
    private String[] allEndpoints;

    private static final long TIMEOUT = 1000 * 95;
    private UiDevice mDevice = UiDevice.getInstance(getInstrumentation());


    @Rule
    public ActivityTestRule<ViewAlarms> activityTestRule = new ActivityTestRule<>(ViewAlarms.class);

    @Before
    public void setup() throws TimeoutException, InterruptedException {
        clickCreateAlarmButton();
    }

    @After
    public void cleanup() {
        mDevice.findObject(By.text("CLEAR ALL")).click();
    }

    @Test
    public void testPushNotificationSuccess_withPredictions() throws InterruptedException {
        selectedRoute = "Green Line C";
        selectedDirection = "Eastbound";
        selectedStop = "Park Street";
        selectedEndpoints = new String[]{"North Station"};
        allEndpoints = new String[]{"Lechmere", "North Station"};

        insertAlarm();
        mDevice.openNotification();
        mDevice.wait(Until.hasObject(By.text(selectedStop)), TIMEOUT);

        UiObject2 subtext = mDevice.findObject(By.text(selectedRoute));
        UiObject2 title = mDevice.findObject(By.text(selectedStop));
        UiObject2 text = mDevice.findObject(By.text(
            "1:04 pm (North Station)\n" +
            "1:22 pm (North Station)"
        ));

        Arrays.asList(subtext, title, text).forEach(Assert::assertNotNull);
    }

    @Test
    public void testPushNotificationSuccess_noPredictions() throws InterruptedException {
        selectedRoute = "Orange Line";
        selectedDirection = "Northbound";
        selectedStop = "Forest Hills";
        selectedEndpoints = new String[]{"Oak Grove"};
        allEndpoints = new String[]{"Oak Grove", "Oak Grove (Shuttle)", "Sullivan", "Sullivan Square"};

        insertAlarm();
        mDevice.openNotification();
        mDevice.wait(Until.hasObject(By.text(selectedStop)), TIMEOUT);

        UiObject2 subtext = mDevice.findObject(By.text(selectedRoute));
        UiObject2 title = mDevice.findObject(By.text(selectedStop));
        UiObject2 text =  mDevice.findObject(By.text("No trains arriving/departing soon."));

        Arrays.asList(subtext, title, text).forEach(Assert::assertNotNull);
    }

    @Test
    public void testPushNotificationError() throws InterruptedException {
        selectedRoute = "Red Line";
        selectedDirection = "Northbound";
        selectedStop = "South Station";
        selectedEndpoints = new String[]{"Alewife"};
        allEndpoints = new String[]{"Alewife"};

        insertAlarm();
        mDevice.openNotification();
        mDevice.wait(Until.hasObject(By.text(selectedStop)), TIMEOUT);

        UiObject2 subtext = mDevice.findObject(By.text(selectedRoute));
        UiObject2 title = mDevice.findObject(By.text("Error fetching MBTA arrival times for " + selectedStop + "."));
        UiObject2 text = mDevice.findObject(By.text("Please check your internet connection."));

        Arrays.asList(subtext, title, text).forEach(Assert::assertNotNull);
    }

    private void clickCreateAlarmButton() {
        onView(withId(R.id.add_alarm)).check(matches(isDisplayed())).perform(click());
    }

    private void insertAlarm() throws InterruptedException {
        // Create a new alarm with MBTA data.
        BaseEditAlarmSectionTest.moveToMbtaSettingsTab();
        AlarmRouteTest.selectRoute(selectedRoute);
        AlarmDirectionTest.selectDirection(selectedDirection);
        AlarmStopTest.selectStop(selectedStop);
        AlarmEndpointsTest.selectEndpoints(selectedEndpoints, allEndpoints);
        BaseEditAlarmSectionTest.saveAlarm();

        // Update the existing alarm's time.
        onView(withText(selectedStop)).perform(click());
        LocalTime now = LocalDateTime.now().toLocalTime();
        int extraMinutes = now.getSecond() <= 55 ? 1 : 2;
        now = now.plusMinutes(extraMinutes);
        AlarmTimeTest.selectTime(now.getHour(), now.getMinute());
        BaseEditAlarmSectionTest.saveAlarm();
    }
}
