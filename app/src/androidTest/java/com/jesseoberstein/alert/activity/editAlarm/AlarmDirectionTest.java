package com.jesseoberstein.alert.activity.editAlarm;

import com.jesseoberstein.alert.R;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.jesseoberstein.alert.activity.editAlarm.AlarmEndpointsTest.confirmEndpointsSelected;
import static com.jesseoberstein.alert.activity.editAlarm.AlarmEndpointsTest.selectEndpoints;
import static org.hamcrest.CoreMatchers.not;

@Ignore("Tests that select a route are flaky")
public class AlarmDirectionTest extends BaseEditAlarmSectionTest {
    private static final String SELECTED_ROUTE = "Green Line B";
    private static final String SELECTED_DIRECTION = "Eastbound";
    private static final String[] ALL_ENDPOINTS = {"Park Street", "Lechmere", "Washington Street (Shuttle)"};
    private static final String[] SELECTED_ENDPOINTS = {"Park Street"};
    private static final String SELECTED_ENDPOINT_STRING = "Park Street";

    @Before
    public void prepare() throws InterruptedException {
        moveToMbtaSettingsTab();
//        onView(withId(R.id.alarmSettings_endpoints)).check(matches(not(isDisplayed())));
        AlarmRouteTest.selectRoute(SELECTED_ROUTE);
        relaunchActivity();
        confirmDirectionLabelAndDefaultValue();
    }

    @Test
    public void selectingSameDirectionDoesNotResetEndpoints() throws InterruptedException {
        selectDirection(SELECTED_DIRECTION);
        confirmDirectionSelected(SELECTED_DIRECTION);

        selectEndpoints(SELECTED_ENDPOINTS, ALL_ENDPOINTS);
        selectDirection(SELECTED_DIRECTION);
        confirmEndpointsSelected(SELECTED_ENDPOINTS, ALL_ENDPOINTS, SELECTED_ENDPOINT_STRING);
    }

    @Test
    public void invalidDirectionShowsValidationError() throws InterruptedException {
        verifyErrorMessageShowsOnSave(R.string.direction_invalid);
        selectDirectionFromList(SELECTED_DIRECTION);
        verifyErrorMessageNotShownOnSave(R.string.direction_invalid);
    }

    public static void selectDirection(String directionName) throws InterruptedException {
        openDirectionDialog();
        selectDirectionFromList(directionName);
    }

    private static void openDirectionDialog() {
        openSectionDialog(R.id.alarmSettings_direction);
    }

    private static void selectDirectionFromList(String directionName) throws InterruptedException {
        selectItem(directionName);
    }

    private void confirmDirectionSelected(String directionName) throws InterruptedException {
        // Check that the direction string is correct.
        onView(withId(R.id.alarmSettings_section_value_direction)).check(matches(withText(directionName)));
        openDirectionDialog();

        // Check that the direction selection persists in the dialog.
        onView(withText(directionName)).check(matches(isChecked()));
        selectDirectionFromList(directionName);
    }

    private void confirmDirectionLabelAndDefaultValue() {
        confirmSectionLabelAndDefaultHint(
            R.id.alarmSettings_section_label_direction, R.string.direction,
            R.id.alarmSettings_section_value_direction, R.string.direction_default
        );
    }
}
