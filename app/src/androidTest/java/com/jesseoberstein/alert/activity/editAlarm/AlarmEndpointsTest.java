package com.jesseoberstein.alert.activity.editAlarm;

import com.jesseoberstein.alert.R;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.jesseoberstein.alert.activity.editAlarm.AlarmDirectionTest.selectDirection;
import static com.jesseoberstein.alert.activity.editAlarm.AlarmRouteTest.selectRoute;
import static com.jesseoberstein.alert.activity.editAlarm.AlarmStopTest.selectStop;
import static java.util.stream.Collectors.partitioningBy;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.containsString;

@Ignore("Tests that select a route are flaky")
public class AlarmEndpointsTest extends BaseEditAlarmSectionTest {
    private String[] testSelection;
    private String[] testFullEndpointList = {"Attleboro", "Providence", "Stoughton", "Wickford Junction"};

    @Before
    public void prepare() throws InterruptedException {
        moveToMbtaSettingsTab();
        selectRoute("Providence/Stoughton Line");
        relaunchActivity();
        selectDirection("Outbound");
        selectStop("Ruggles");
        confirmEndpointLabelAndDefaultValue(testFullEndpointList);
//        onView(withId(R.id.alarmSettings_endpoints)).check(matches(isDisplayed()));
    }

    @Test
    public void endpointsSectionLabelAndValue_singleEndpoint() throws InterruptedException {
        testSelection = new String[]{"Providence"};
        selectEndpoints(testSelection, testFullEndpointList);
        confirmEndpointsSelected(testSelection, testFullEndpointList, "Providence");
    }

    @Test
    public void endpointsSectionLabelAndValue_multipleEndpoints() throws InterruptedException {
        testSelection = new String[]{"Stoughton", "Wickford Junction", "Attleboro"};
        selectEndpoints(testSelection, testFullEndpointList);
        confirmEndpointsSelected(testSelection, testFullEndpointList, "Attleboro,  Stoughton,  Wickford Junction");
    }

    @Test
    public void invalidEndpointsShowsValidationError() throws InterruptedException {
        openEndpointDialog();
        selectEndpointsFromList(new String[]{}, testFullEndpointList);
        verifyErrorMessageShowsOnSave(R.string.endpoints_invalid);
        selectEndpointsFromList(new String[]{"Stoughton"}, testFullEndpointList);
        saveAlarm();
        assertTrue(activityRule.getActivity().isFinishing());
    }

    public static void selectEndpoints(String[] selected, String[] fullList) throws InterruptedException {
        openEndpointDialog();
        checkEndpointsSelected(fullList);
        selectEndpointsFromList(selected, fullList);
    }

    static void confirmEndpointsSelected(String[] selected, String[] fullList, String endpointString) {
        // Check that the endpoint string is correct.
//        onView(withId(R.id.alarmSettings_section_value_endpoints)).check(matches(withText(endpointString)));
        openEndpointDialog();

        List<String> selectedList = Arrays.asList(selected);
        Map<Boolean, List<String>> isSelected = Arrays.stream(fullList).collect(partitioningBy(selectedList::contains));

        // Check that the selected endpoints are checked in the dialog.
        isSelected.get(true).forEach(name -> onView(withText(name)).check(matches(isChecked())));

        // Check that not selected endpoints are checked in the dialog.
        isSelected.get(false).forEach(name -> onView(withText(name)).check(matches(isNotChecked())));
    }

    private static void openEndpointDialog() {
//        openSectionDialog(R.id.alarmSettings_endpoints);
    }

    private static void checkEndpointsSelected(String[] endpoints) {
        Arrays.stream(endpoints).forEach(name -> onView(withText(name)).check(matches(isChecked())));
    }

    private static void selectEndpointsFromList(String[] selected, String[] fullList) throws InterruptedException {
        // Assuming all endpoints are already selected, de-select the ones that shouldn't be selected.
        String[] endpointsToDeselect = Arrays.stream(fullList)
                .filter(endpoint -> !Arrays.asList(selected).contains(endpoint))
                .toArray(String[]::new);

        selectItems(endpointsToDeselect);
    }

    private void confirmEndpointLabelAndDefaultValue(String[] allEndpoints) {
//        confirmSectionLabel(R.id.alarmSettings_section_label_endpoints, R.string.endpoints);

        // Check default endpoints is all endpoints available.
//        Arrays.stream(allEndpoints).forEach(endpoint ->
//                onView(withId(R.id.alarmSettings_section_value_endpoints))
//                        .check(matches(withText(containsString(endpoint)))));
    }
}
