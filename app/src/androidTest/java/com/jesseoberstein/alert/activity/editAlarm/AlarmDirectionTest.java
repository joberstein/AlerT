package com.jesseoberstein.alert.activity.editAlarm;

import com.jesseoberstein.alert.R;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.jesseoberstein.alert.utils.Constants.DELAY_DIALOG_DISMISS;

public class AlarmDirectionTest extends BaseEditAlarmTest {

    @Test
    public void directionSectionLabelAndValue() throws InterruptedException {
        moveToMbtaSettingsTab();
        AlarmRouteTest.selectRoute("Or", "Orange Line");
        relaunchActivity();
        AlarmDirectionTest.selectDirection("Northbound");
        confirmDirectionSelected("Northbound");
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

    private void confirmDirectionSelected(String directionName) {
        onView(withId(R.id.alarmSettings_section_value_direction))
                .check(matches(withText(directionName)));
    }
}
