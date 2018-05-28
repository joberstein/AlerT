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
import static java.util.stream.Collectors.partitioningBy;
import static org.hamcrest.Matchers.containsString;

public class AlarmEndpointsTest extends BaseEditAlarmTest {
    private String[] testSelection;
    private String[] testFullEndpointList = {"Attleboro", "Providence", "Stoughton", "Wickford Junction"};

    @Test
    public void endpointsSectionLabelAndValue_singleEndpoint() throws InterruptedException {
        testSelection = new String[]{"Providence"};
        prepareForEndpointTest();
        confirmEndpointsSelected(testSelection, testFullEndpointList, "Providence");
    }

    @Test
    public void endpointsSectionLabelAndValue_multipleEndpoints() throws InterruptedException {
        testSelection = new String[]{"Stoughton", "Wickford Junction", "Attleboro"};
        prepareForEndpointTest();
        confirmEndpointsSelected(testSelection, testFullEndpointList, "Attleboro,  Stoughton,  Wickford Junction");
    }

    private void prepareForEndpointTest() throws InterruptedException {
        moveToMbtaSettingsTab();
        AlarmRouteTest.openRouteDialog();
        AlarmRouteTest.selectRoute("Providence/Stoughton Line");
        relaunchActivity();

        AlarmDirectionTest.openDirectionDialog();
        AlarmDirectionTest.selectDirection("Outbound");
        AlarmStopTest.openStopDialog();
        AlarmStopTest.selectStop("Ruggles");

        openEndpointDialog();
        checkEndpointsSelected(testFullEndpointList);
        selectEndpoints(testSelection, testFullEndpointList);
    }

    private void checkEndpointsSelected(String[] endpoints) {
        Arrays.stream(endpoints).forEach(name -> onView(withText(name)).check(matches(isChecked())));
    }

    static void openEndpointDialog() {
        openSectionDialog(R.id.alarmSettings_endpoints);
    }

    static void selectEndpoints(String[] selected, String[] fullList) throws InterruptedException {
        // Assuming all endpoints are already selected, de-select the ones that shouldn't be selected.
        String[] endpointsToDeselect = Arrays.stream(fullList)
                .filter(endpoint -> !Arrays.asList(selected).contains(endpoint))
                .toArray(String[]::new);

        selectItems(endpointsToDeselect);
    }

    static void confirmEndpointsSelected(String[] selected, String[] fullList, String endpointString) {
        // Check that the endpoint string is correct.
        onView(withId(R.id.alarmSettings_section_value_endpoints)).check(matches(withText(endpointString)));
        openEndpointDialog();

        List<String> selectedList = Arrays.asList(selected);
        Map<Boolean, List<String>> isSelected = Arrays.stream(fullList).collect(partitioningBy(selectedList::contains));

        // Check that the selected endpoints are checked in the dialog.
        isSelected.get(true).forEach(name -> onView(withText(name)).check(matches(isChecked())));

        // Check that not selected endpoints are checked in the dialog.
        isSelected.get(false).forEach(name -> onView(withText(name)).check(matches(isNotChecked())));
    }

    private void confirmEndpointLabelAndDefaultValue(String[] allEndpoints) {
        confirmSectionLabel(R.id.alarmSettings_section_label_endpoints, R.string.endpoints);

        // Check default endpoints is all endpoints available.
        Arrays.stream(allEndpoints).forEach(endpoint ->
                onView(withId(R.id.alarmSettings_section_value_endpoints))
                        .check(matches(withText(containsString(endpoint)))));
    }
}
