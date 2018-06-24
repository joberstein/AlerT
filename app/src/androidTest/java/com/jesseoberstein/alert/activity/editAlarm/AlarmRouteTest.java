package com.jesseoberstein.alert.activity.editAlarm;

import com.jesseoberstein.alert.R;

import org.junit.Before;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.jesseoberstein.alert.activity.editAlarm.AlarmDirectionTest.selectDirection;
import static org.hamcrest.CoreMatchers.not;

public class AlarmRouteTest extends BaseEditAlarmSectionTest {
    private String selectedRoute = "Orange Line";

    @Before
    public void prepare() throws InterruptedException {
        moveToMbtaSettingsTab();
        confirmRouteLabelAndDefaultValue();

        // Check that the other mbta fields aren't visible without a route.
        onView(withId(R.id.alarmSettings_stop)).check(matches(not(isDisplayed())));
        onView(withId(R.id.alarmSettings_direction)).check(matches(not(isDisplayed())));
        onView(withId(R.id.alarmSettings_endpoints)).check(matches(not(isDisplayed())));
    }

    @Test
    public void mbtaInfoResetsWhenRouteIsChanged() throws InterruptedException {
        selectAndConfirmRoute();
        selectDirection("Northbound");

        selectedRoute = "Red Line";
        selectAndConfirmRoute();

        onView(withId(R.id.alarmSettings_section_value_direction)).check(matches(withHint(R.string.direction_default)));
        onView(withId(R.id.alarmSettings_stop)).check(matches(not(isDisplayed())));
        onView(withId(R.id.alarmSettings_endpoints)).check(matches(not(isDisplayed())));
    }

    @Test
    public void invalidRouteShowsValidationError() throws InterruptedException {
        verifyErrorMessageShowsOnSave(R.string.route_invalid);
        selectRouteFromAutosuggest(selectedRoute);
        relaunchActivity();
        verifyErrorMessageNotShownOnSave(R.string.route_invalid);
    }

    public static void selectRoute(String routeName)  throws InterruptedException {
        openRouteDialog();
        selectRouteFromAutosuggest(routeName);
    }

    private void selectAndConfirmRoute() throws InterruptedException {
        selectRoute(selectedRoute);
        relaunchActivity();
        confirmRouteSelected(selectedRoute);
    }

    private static void openRouteDialog() {
        openSectionDialog(R.id.alarmSettings_route);
    }

    private static void selectRouteFromAutosuggest(String routeName) throws InterruptedException {
        selectAutosuggestItem(R.id.alarm_route, routeName);
    }

    private void confirmRouteSelected(String routeName) throws InterruptedException {
        // Check that the stop string is correct.
        onView(withId(R.id.alarmSettings_section_value_route)).check(matches(withText(routeName)));
        openRouteDialog();

        // Check that the selected stop persists in the dialog.
        onView(withId(R.id.alarm_route)).check(matches(withText(routeName)));
        selectRouteFromAutosuggest(routeName);
    }

    private void confirmRouteLabelAndDefaultValue() {
        confirmSectionLabelAndDefaultHint(
            R.id.alarmSettings_section_label_route, R.string.route,
            R.id.alarmSettings_section_value_route, R.string.route_default
        );
    }
}
