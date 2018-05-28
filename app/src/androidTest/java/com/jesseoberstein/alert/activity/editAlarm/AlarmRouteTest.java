package com.jesseoberstein.alert.activity.editAlarm;

import com.jesseoberstein.alert.R;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

public class AlarmRouteTest extends BaseEditAlarmTest {
    private String selectedRoute = "Orange Line";

    @Test
    public void mbtaInfoResetsWhenRouteIsChanged() throws InterruptedException {
        prepareForRouteTest();

        AlarmDirectionTest.openDirectionDialog();
        AlarmDirectionTest.selectDirection("Northbound");

        openRouteDialog();
        selectRoute("Red Line");
        relaunchActivity();

        onView(withId(R.id.alarmSettings_section_value_direction)).check(matches(withHint(R.string.direction_default)));
        onView(withId(R.id.alarmSettings_stop)).check(matches(not(isDisplayed())));
        onView(withId(R.id.alarmSettings_endpoints)).check(matches(not(isDisplayed())));
    }

    private void prepareForRouteTest() throws InterruptedException {
        moveToMbtaSettingsTab();
        confirmRouteLabelAndDefaultValue();

        // Check that the other mbta fields aren't visible without a route.
        onView(withId(R.id.alarmSettings_stop)).check(matches(not(isDisplayed())));
        onView(withId(R.id.alarmSettings_direction)).check(matches(not(isDisplayed())));
        onView(withId(R.id.alarmSettings_endpoints)).check(matches(not(isDisplayed())));

        openRouteDialog();
        selectRoute(selectedRoute);
        relaunchActivity();

        // Check that the direction section shows now that a route is selected.
        onView(withId(R.id.alarmSettings_direction)).check(matches(isDisplayed()));
        confirmRouteSelected("Orange Line");
    }

    static void openRouteDialog() {
        openSectionDialog(R.id.alarmSettings_route);
    }

    static void selectRoute(String routeName) throws InterruptedException {
        selectAutosuggestItem(R.id.alarm_route, routeName);
    }

    private void confirmRouteSelected(String routeName) throws InterruptedException {
        // Check that the stop string is correct.
        onView(withId(R.id.alarmSettings_section_value_route)).check(matches(withText(routeName)));
        openRouteDialog();

        // Check that the selected stop persists in the dialog.
        onView(withId(R.id.alarm_route)).check(matches(withText(routeName)));
        selectRoute(routeName);
    }

    private void confirmRouteLabelAndDefaultValue() {
        confirmSectionLabelAndDefaultHint(
            R.id.alarmSettings_section_label_route, R.string.route,
            R.id.alarmSettings_section_value_route, R.string.route_default
        );
    }
}
