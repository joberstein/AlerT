package com.jesseoberstein.alert.activity.editAlarm;

import com.jesseoberstein.alert.R;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class AlarmRepeatTest extends BaseEditAlarmTest {

    @Test
    public void repeatSectionLabelAndValue() {
        moveToTimeSettingsTab();

        // Check repeat label text.
        onView(withId(R.id.alarmSettings_section_label_repeat))
                .check(matches(withText(R.string.repeat)));

        // Check default repeat value.
        onView(withId(R.id.alarmSettings_section_value_repeat)).check(matches(withText("Never")));

        // Click on repeat section.
        onView(withId(R.id.alarmSettings_repeat)).perform(click());

        // Select 'Custom' repeat.
        onView(withText("Custom")).perform(click());

        // Toggle 'Monday' checkbox.
        onView(withText("Monday")).perform(click()).check(matches(isChecked()));

        // Save day selection.
        onView(withText(R.string.ok)).perform(click());

        // Confirm that alarm repeat has been saved.
        onView(withId(R.id.alarmSettings_section_value_repeat))
                .check(matches(withText("Custom")));
    }
}
