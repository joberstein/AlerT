package com.jesseoberstein.alert.activity.editAlarm;

import com.jesseoberstein.alert.R;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.jesseoberstein.alert.utils.Constants.DELAY_DIALOG_DISMISS;
import static java.util.stream.Collectors.partitioningBy;
import static org.hamcrest.Matchers.containsString;

public class AlarmEndpointsTest extends BaseEditAlarmTest {
    private String[] testSelection;
    private String[] testFullEndpointList = {"Attleboro", "Providence", "Stoughton", "Wickford Junction"};

    @Test
    public void endpointsSectionLabelAndValue_singleEndpoint() throws InterruptedException {
        prepareForEndpointSelection();
        testSelection = new String[]{"Providence"};
        AlarmEndpointsTest.selectEndpoints(testSelection, testFullEndpointList);
        confirmEndpointsSelected(testSelection, testFullEndpointList, "Providence");
    }

    @Test
    public void endpointsSectionLabelAndValue_multipleEndpoints() throws InterruptedException {
        prepareForEndpointSelection();
        testSelection = new String[]{"Stoughton", "Wickford Junction", "Attleboro"};
        AlarmEndpointsTest.selectEndpoints(testSelection, testFullEndpointList);
        confirmEndpointsSelected(testSelection, testFullEndpointList, "Attleboro,  Stoughton,  Wickford Junction");
    }

    private void prepareForEndpointSelection() throws InterruptedException {
        moveToMbtaSettingsTab();
        AlarmRouteTest.selectRoute("Pr", "Providence/Stoughton Line");
        relaunchActivity();
        AlarmStopTest.selectStop("Ru", "Ruggles");
        AlarmDirectionTest.selectDirection("Outbound");
    }

    static void selectEndpoints(String[] selected, String[] fullList) throws InterruptedException {
        // Check endpoints label.
        onView(withId(R.id.alarmSettings_section_label_endpoints))
                .check(matches(withText(R.string.endpoints)));

        // Check default endpoints is all endpoints available.
        Arrays.stream(fullList).forEach(endpoint ->
                onView(withId(R.id.alarmSettings_section_value_endpoints))
                        .check(matches(withText(containsString(endpoint)))));

        // Click endpoints section.
        onView(withId(R.id.alarmSettings_endpoints)).perform(click());

        // Check all endpoints are toggled by default.
        Arrays.stream(fullList).forEach(name ->
                onView(withText(name)).check(matches(isChecked())));

        // Click on endpoints that should not be selected.
        Arrays.stream(fullList)
                .filter(endpoint -> !Arrays.asList(selected).contains(endpoint))
                .forEach(name -> onView(withText(name)).perform(click()).check(matches(isNotChecked())));

        // Check that the selected endpoints are still checked.
        Arrays.stream(selected).forEach(name ->
                onView(withText(name)).check(matches(isChecked())));

        // Save endpoint selection.
        onView(withText(R.string.ok)).perform(click());

        // Wait until the dialog is dismissed.
        Thread.sleep(DELAY_DIALOG_DISMISS);
    }

    static void confirmEndpointsSelected(String[] selected, String[] fullList, String endpointString) {
        // Check that the endpoint string is correct.
        onView(withId(R.id.alarmSettings_section_value_endpoints))
                .check(matches(withText(endpointString)))
                .perform(click());

        List<String> selectedList = Arrays.asList(selected);
        Map<Boolean, List<String>> isSelected = Arrays.stream(fullList).collect(partitioningBy(selectedList::contains));

        // Check that the selected endpoints are checked in the dialog.
        isSelected.get(true).forEach(name -> onView(withText(name)).check(matches(isChecked())));

        // Check that not selected endpoints are checked in the dialog.
        isSelected.get(false).forEach(name -> onView(withText(name)).check(matches(isNotChecked())));
    }
}
