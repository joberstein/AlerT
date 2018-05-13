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

        // Check duration label text.
        onView(withId(R.id.alarmSettings_section_label_duration))
                .check(matches(withText(R.string.duration)));

        // Check default duration value.
        onView(withId(R.id.alarmSettings_section_value_duration))
                .check(matches(withText(DEFAULT_DURATION)));

        // Click on duration section.
        onView(withId(R.id.alarmSettings_duration)).perform(click());

        // Select new duration.
        onView(withText(SELECTED_DURATION)).perform(click());

        // Wait until the dialog is dismissed.
        Thread.sleep(DELAY_DIALOG_DISMISS);

        // Confirm that alarm duration has been saved.
        onView(withId(R.id.alarmSettings_section_value_duration))
                .check(matches(withText(SELECTED_DURATION)))
                .perform(click());

        // Check that the duration persists in the dialog.
        onView(withText(SELECTED_DURATION)).check(matches(isChecked()));
    }
}
