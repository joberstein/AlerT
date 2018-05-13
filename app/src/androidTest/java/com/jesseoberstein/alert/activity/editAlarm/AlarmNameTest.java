package com.jesseoberstein.alert.activity.editAlarm;

import com.jesseoberstein.alert.R;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class AlarmNameTest extends BaseEditAlarmTest {
    private static final String TEST_NICKNAME = "Test Name";

    @Test
    public void nameSectionLabelAndValue() {
        moveToTimeSettingsTab();

        // Check name label text.
        onView(withId(R.id.alarmSettings_section_label_name))
                .check(matches(withText(R.string.nickname)));

        // Check name default value.
        onView(withId(R.id.alarmSettings_section_value_name))
                .check(matches(withHint(R.string.nickname_default)));

        // Click on name section.
        onView(withId(R.id.alarmSettings_name)).perform(click());

        // Type new alarm name and save.
        onView(withId(R.id.alarm_nickname))
                .check(matches(withHint(R.string.nickname_default)))
                .perform(replaceText(TEST_NICKNAME))
                .perform(pressImeActionButton());

        // Confirm the new alarm name has been saved.
        onView(withId(R.id.alarmSettings_section_value_name))
                .check(matches(withText(TEST_NICKNAME)))
                .perform(click());

        // Check that the new name persists in the dialog.
        onView(withId(R.id.alarm_nickname)).check(matches(withText(TEST_NICKNAME)));
    }
}
