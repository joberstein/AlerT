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
import static com.jesseoberstein.alert.utils.Constants.DELAY_DIALOG_DISMISS;
import static org.hamcrest.CoreMatchers.not;

public class AlarmDirectionTest extends BaseEditAlarmTest {
    private static final String SELECTED_ROUTE_INPUT = "Gr";
    private static final String SELECTED_ROUTE = "Green Line B";
    private static final String SELECTED_DIRECTION = "Eastbound";
    private static final String[] ALL_ENDPOINTS = {"Park Street", "Lechmere"};
    private static final String[] SELECTED_ENDPOINTS = {"Park Street"};
    private static final String SELECTED_ENDPOINT_STRING = "Park Street";

    @Test
    public void directionSectionLabelAndValue() throws InterruptedException {
        moveToMbtaSettingsTab();
        onView(withId(R.id.alarmSettings_endpoints)).check(matches(not(isDisplayed())));

        AlarmRouteTest.selectRoute(SELECTED_ROUTE_INPUT, SELECTED_ROUTE);
        relaunchActivity();
        selectDirection(SELECTED_DIRECTION);

        onView(withId(R.id.alarmSettings_endpoints)).check(matches(isDisplayed()));
        confirmDirectionSelected(SELECTED_DIRECTION);
    }

    @Test
    public void selectingSameDirectionDoesNotResetEndpoints() throws InterruptedException {
        moveToMbtaSettingsTab();
        AlarmRouteTest.selectRoute(SELECTED_ROUTE_INPUT, SELECTED_ROUTE);
        relaunchActivity();
        selectDirection(SELECTED_DIRECTION);
        AlarmEndpointsTest.selectEndpoints(SELECTED_ENDPOINTS, ALL_ENDPOINTS);
        selectDirection(SELECTED_DIRECTION);
        AlarmEndpointsTest.confirmEndpointsSelected(SELECTED_ENDPOINTS, ALL_ENDPOINTS, SELECTED_ENDPOINT_STRING);
    }

    static void selectDirection(String directionName) throws InterruptedException {
        // Check direction label.
        onView(withId(R.id.alarmSettings_section_label_direction))
                .check(matches(withText(R.string.direction)));

        // Check default direction value.
        onView(withId(R.id.alarmSettings_section_value_direction))
                .check(matches(withHint(R.string.direction_default)));

        // Click direction section.
        onView(withId(R.id.alarmSettings_direction)).perform(click());

        // Click on auto-suggested direction.
        onView(withText(directionName)).perform(click());

        // Wait until the dialog is dismissed.
        Thread.sleep(DELAY_DIALOG_DISMISS);
    }

    private void confirmDirectionSelected(String directionName) throws InterruptedException {
        // Check that the direction string is correct.
        onView(withId(R.id.alarmSettings_section_value_direction))
                .check(matches(withText(directionName)))
                .perform(click());

        // Check that the direction selection persists in the dialog.
        onView(withText(directionName)).check(matches(isChecked()));
    }
}
