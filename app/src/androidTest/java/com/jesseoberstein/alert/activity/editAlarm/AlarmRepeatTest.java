package com.jesseoberstein.alert.activity.editAlarm;

import com.jesseoberstein.alert.R;

import org.junit.Test;

import java.util.Arrays;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class AlarmRepeatTest extends BaseEditAlarmTest {
    private static final String DEFAULT_REPEAT = "Never";
    private static final String SELECTED_REPEAT = "Custom";
    private static final String[] SELECTED_DAYS = {"Monday", "Wednesday", "Saturday"};

    @Test
    public void repeatSectionLabelAndValue() {
        moveToTimeSettingsTab();

        // Check repeat label text.
        onView(withId(R.id.alarmSettings_section_label_repeat))
                .check(matches(withText(R.string.repeat)));

        // Check default repeat value.
        onView(withId(R.id.alarmSettings_section_value_repeat)).check(matches(withText(DEFAULT_REPEAT)));

        // Click on repeat section.
        onView(withId(R.id.alarmSettings_repeat)).perform(click());

        // Select 'Custom' repeat.
        onView(withText(SELECTED_REPEAT)).perform(click());

        // Toggle 'Monday', 'Wednesday', and 'Saturday' checkboxes.
        Arrays.stream(SELECTED_DAYS).forEach(day ->
                onView(withText(day)).perform(click()).check(matches(isChecked())));

        // Save day selection.
        onView(withText(R.string.ok)).perform(click());

        // Confirm that alarm repeat has been saved.
        onView(withId(R.id.alarmSettings_section_value_repeat))
                .check(matches(withText(SELECTED_REPEAT)))
                .perform(click());

        // Check that the repeat type persists in the dialog.
        onView(withText(SELECTED_REPEAT)).check(matches(isChecked())).perform(click());

        // Check that the day selection persists in the dialog.
        Arrays.stream(SELECTED_DAYS).forEach(day ->
                onView(withText(day)).check(matches(isChecked())));

    }
}
