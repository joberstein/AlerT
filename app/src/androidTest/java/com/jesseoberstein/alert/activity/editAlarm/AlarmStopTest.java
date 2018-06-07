package com.jesseoberstein.alert.activity.editAlarm;

import android.support.test.espresso.matcher.RootMatchers;

import com.jesseoberstein.alert.R;

import org.junit.Before;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.jesseoberstein.alert.activity.editAlarm.AlarmDirectionTest.*;
import static com.jesseoberstein.alert.activity.editAlarm.AlarmRouteTest.*;
import static junit.framework.Assert.assertTrue;

public class AlarmStopTest extends BaseEditAlarmSectionTest {
    private String selectedRoute = "Red Line";
    private String selectedStop = "Broadway";

    @Before
    public void prepare() throws InterruptedException {
        moveToMbtaSettingsTab();
        selectRoute(selectedRoute);
        relaunchActivity();
        selectDirection("Southbound");
        confirmStopLabelAndDefaultValue();
    }

    @Test
    public void stopThatIsAlsoEndpointIsNotSelectable() throws InterruptedException {
        selectStop(selectedStop);
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
        selectedStop = "Downtown Crossing";
        selectStop(selectedStop);
        selectDirection("Northbound");
        confirmStopSelected(selectedStop);
    }

    @Test
    public void stopResetWhenNotPresentForSelectedDirection() throws InterruptedException {
        selectedStop = "Alewife";
        selectStop(selectedStop);
        selectDirection("Northbound");

        // Stop value should reset to the default value.
        confirmStopLabelAndDefaultValue();
    }

    @Test
    public void selectingSameRouteDoesNotResetStop() throws InterruptedException {
        selectStop(selectedStop);
        selectRoute(selectedRoute);
        confirmStopSelected(selectedStop);
    }


    @Test
    public void invalidStopShowsValidationError() throws InterruptedException {
        verifyErrorMessageShowsOnSave(R.string.stop_invalid);
        selectStopFromAutosuggest(selectedStop);
        saveAlarm();
        assertTrue(activityRule.getActivity().isFinishing());
    }

    static void selectStop(String stopName) throws InterruptedException {
        openStopDialog();
        selectStopFromAutosuggest(stopName);
    }

    private static void openStopDialog() {
        openSectionDialog(R.id.alarmSettings_stop);
    }

    private static void selectStopFromAutosuggest(String stopName) throws InterruptedException {
        selectAutosuggestItem(R.id.alarm_stop, stopName);
    }

    private void confirmStopSelected(String stopName) throws InterruptedException {
        // Check that the stop string is correct.
        onView(withId(R.id.alarmSettings_section_value_stop)).check(matches(withText(stopName)));
        openStopDialog();

        // Check that the selected stop persists in the dialog.
        onView(withId(R.id.alarm_stop)).check(matches(withText(stopName)));
        selectStopFromAutosuggest(stopName);
    }

    private void confirmStopLabelAndDefaultValue() {
        confirmSectionLabelAndDefaultHint(
            R.id.alarmSettings_section_label_stop, R.string.stop,
            R.id.alarmSettings_section_value_stop, R.string.stop_default
        );
    }
}
