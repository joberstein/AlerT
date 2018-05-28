package com.jesseoberstein.alert.activity.editAlarm;

import android.support.test.espresso.matcher.RootMatchers;

import com.jesseoberstein.alert.R;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class AlarmStopTest extends BaseEditAlarmTest {
    private String selectedRoute = "Red Line";
    private String selectedDirection = "Southbound";
    private String selectedStop = "Broadway";

    @Test
    public void stopThatIsAlsoEndpointIsNotSelectable() throws InterruptedException {
        prepareForStopTest();
        openStopDialog();

        // 'Braintree' endpoint is not in the list of stops for 'Southbound'.
        onView(withId(R.id.alarm_stop)).perform(replaceText("Br"));
        onView(withText("Braintree"))
                .inRoot(RootMatchers.isPlatformPopup())
                .check(doesNotExist());

        // 'Alewife' endpoint is in the list of stops for 'Southbound'.
        onView(withId(R.id.alarm_stop)).perform(replaceText("Ale"));
        onView(withText("Alewife"))
                .inRoot(RootMatchers.isPlatformPopup())
                .check(matches(isDisplayed()));
    }

    @Test
    public void stopExistingForBothDirectionsStaysSelected() throws InterruptedException {
        selectedRoute = "Orange Line";
        selectedDirection = "Southbound";
        selectedStop = "Haymarket";
        prepareForStopTest();

        AlarmDirectionTest.openDirectionDialog();
        AlarmDirectionTest.selectDirection("Northbound");
        confirmStopSelected(selectedStop);
    }

    @Test
    public void stopResetWhenNotPresentForSelectedDirection() throws InterruptedException {
        selectedRoute = "Orange Line";
        selectedDirection = "Southbound";
        selectedStop = "Oak Grove";
        prepareForStopTest();

        AlarmDirectionTest.openDirectionDialog();
        AlarmDirectionTest.selectDirection("Northbound");

        // Stop value should reset to the default value.
        confirmStopLabelAndDefaultValue();
    }

    @Test
    public void selectingSameRouteDoesNotResetStop() throws InterruptedException {
        prepareForStopTest();

        AlarmRouteTest.openRouteDialog();
        AlarmRouteTest.selectRoute(selectedRoute);
        confirmStopSelected(selectedStop);
    }

    private void prepareForStopTest() throws InterruptedException {
        moveToMbtaSettingsTab();
        confirmStopLabelAndDefaultValue();

        AlarmRouteTest.openRouteDialog();
        AlarmRouteTest.selectRoute(selectedRoute);
        relaunchActivity();

        AlarmDirectionTest.openDirectionDialog();
        AlarmDirectionTest.selectDirection(selectedDirection);

        openStopDialog();
        selectStop(selectedStop);
        confirmStopSelected(selectedStop);
    }

    static void openStopDialog() {
        openSectionDialog(R.id.alarmSettings_stop);
    }

    static void selectStop(String stopName) throws InterruptedException {
        selectAutosuggestItem(R.id.alarm_stop, stopName);
    }

    private void confirmStopSelected(String stopName) throws InterruptedException {
        // Check that the stop string is correct.
        onView(withId(R.id.alarmSettings_section_value_stop)).check(matches(withText(stopName)));
        openStopDialog();

        // Check that the selected stop persists in the dialog.
        onView(withId(R.id.alarm_stop)).check(matches(withText(stopName)));
        selectStop(stopName);
    }

    private void confirmStopLabelAndDefaultValue() {
        confirmSectionLabelAndDefaultHint(
            R.id.alarmSettings_section_label_stop, R.string.stop,
            R.id.alarmSettings_section_value_stop, R.string.stop_default
        );
    }
}
