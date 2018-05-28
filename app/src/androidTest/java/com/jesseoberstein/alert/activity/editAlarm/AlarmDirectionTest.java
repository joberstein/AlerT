package com.jesseoberstein.alert.activity.editAlarm;

import com.jesseoberstein.alert.R;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

public class AlarmDirectionTest extends BaseEditAlarmTest {
    private static final String SELECTED_ROUTE = "Green Line B";
    private static final String SELECTED_DIRECTION = "Eastbound";
    private static final String[] ALL_ENDPOINTS = {"Park Street", "Lechmere"};
    private static final String[] SELECTED_ENDPOINTS = {"Park Street"};
    private static final String SELECTED_ENDPOINT_STRING = "Park Street";

    @Test
    public void selectingSameDirectionDoesNotResetEndpoints() throws InterruptedException {
        prepareForDirectionTest();

        AlarmEndpointsTest.openEndpointDialog();
        AlarmEndpointsTest.selectEndpoints(SELECTED_ENDPOINTS, ALL_ENDPOINTS);

        openDirectionDialog();
        selectDirection(SELECTED_DIRECTION);
        AlarmEndpointsTest.confirmEndpointsSelected(SELECTED_ENDPOINTS, ALL_ENDPOINTS, SELECTED_ENDPOINT_STRING);
    }

    private void prepareForDirectionTest() throws InterruptedException {
        moveToMbtaSettingsTab();
        onView(withId(R.id.alarmSettings_endpoints)).check(matches(not(isDisplayed())));
        AlarmRouteTest.openRouteDialog();
        AlarmRouteTest.selectRoute(SELECTED_ROUTE);
        relaunchActivity();

        confirmDirectionLabelAndDefaultValue();
        openDirectionDialog();
        selectDirection(SELECTED_DIRECTION);
        confirmDirectionSelected(SELECTED_DIRECTION);

        onView(withId(R.id.alarmSettings_endpoints)).check(matches(isDisplayed()));
    }

    static void openDirectionDialog() {
        openSectionDialog(R.id.alarmSettings_direction);
    }

    static void selectDirection(String directionName) throws InterruptedException {
        selectItem(directionName);
    }

    private void confirmDirectionSelected(String directionName) throws InterruptedException {
        // Check that the direction string is correct.
        onView(withId(R.id.alarmSettings_section_value_direction)).check(matches(withText(directionName)));
        openDirectionDialog();

        // Check that the direction selection persists in the dialog.
        onView(withText(directionName)).check(matches(isChecked()));
        selectDirection(directionName);
    }

    private void confirmDirectionLabelAndDefaultValue() {
        confirmSectionLabelAndDefaultHint(
            R.id.alarmSettings_section_label_direction, R.string.direction,
            R.id.alarmSettings_section_value_direction, R.string.direction_default
        );
    }
}
