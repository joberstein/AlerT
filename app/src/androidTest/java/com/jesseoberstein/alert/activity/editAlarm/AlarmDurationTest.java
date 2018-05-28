package com.jesseoberstein.alert.activity.editAlarm;

import com.jesseoberstein.alert.R;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.jesseoberstein.alert.utils.Constants.DELAY_DIALOG_DISMISS;

public class AlarmDurationTest extends BaseEditAlarmTest {
    private static final String DEFAULT_DURATION = "30 minutes";
    private static final String SELECTED_DURATION = "1 hour";

    @Test
    public void durationSectionLabelAndValue() throws InterruptedException {
        moveToTimeSettingsTab();
        confirmDurationLabelAndDefaultValue();
        openDurationDialog();
        selectDuration(SELECTED_DURATION);
        confirmDurationSelected(SELECTED_DURATION);
    }

    private void openDurationDialog() {
        openSectionDialog(R.id.alarmSettings_duration);
    }

    private void selectDuration(String duration) throws InterruptedException {
        selectItem(duration);
    }

    private void confirmDurationSelected(String duration) throws InterruptedException {
        // Check that the duration string is correct.
        onView(withId(R.id.alarmSettings_section_value_duration)).check(matches(withText(duration)));
        openDurationDialog();

        // Check that the duration selection persists in the dialog.
        onView(withText(duration)).check(matches(isChecked()));
        selectDuration(duration);
    }

    private void confirmDurationLabelAndDefaultValue() {
        confirmSectionLabelAndDefaultValue(
            R.id.alarmSettings_section_label_duration, R.string.duration,
            R.id.alarmSettings_section_value_duration, DEFAULT_DURATION
        );
    }
}
