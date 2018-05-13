package com.jesseoberstein.alert.activity.editAlarm;

import android.support.test.espresso.matcher.RootMatchers;

import com.jesseoberstein.alert.R;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.jesseoberstein.alert.utils.Constants.DELAY_DIALOG_DISMISS;

public class AlarmStopTest extends BaseEditAlarmTest {

    @Test
    public void stopSectionLabelAndValue() throws InterruptedException {
        moveToMbtaSettingsTab();
        AlarmRouteTest.selectRoute("Or", "Orange Line");
        relaunchActivity();
        AlarmStopTest.selectStop("Fo", "Forest Hills");
        confirmStopSelected("Forest Hills");
    }

    static void selectStop(String input, String stopName) throws InterruptedException {
        // Check stop label.
        onView(withId(R.id.alarmSettings_section_label_stop))
                .check(matches(withText(R.string.stop)));

        // Check default stop value.
        onView(withId(R.id.alarmSettings_section_value_stop))
                .check(matches(withHint(R.string.stop_default)));

        // Click stop section.
        onView(withId(R.id.alarmSettings_stop)).perform(click());

        // Type new stop.
        onView(withId(R.id.alarm_stop)).perform(replaceText(input));

        // Click on auto-suggested stop.
        onView(withText(stopName))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());

        // Wait until the dialog is dismissed.
        Thread.sleep(DELAY_DIALOG_DISMISS);
    }

    static void confirmStopSelected(String stopName) throws InterruptedException {
        // Check that the stop string is saved.
        onView(withId(R.id.alarmSettings_section_value_stop))
                .check(matches(withText(stopName)))
                .perform(click());

        // Check that the selected stop persists in the dialog.
        onView(withId(R.id.alarm_stop)).check(matches(withText(stopName)));
    }
}
