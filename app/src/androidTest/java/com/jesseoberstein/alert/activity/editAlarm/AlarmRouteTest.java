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

public class AlarmRouteTest extends BaseEditAlarmTest {

    @Test
    public void routeSectionLabelAndValue() throws InterruptedException {
        moveToMbtaSettingsTab();

        // Check route label.
        onView(withId(R.id.alarmSettings_section_label_route))
                .check(matches(withText(R.string.route)));

        // Check default route value.
        onView(withId(R.id.alarmSettings_section_value_route))
                .check(matches(withHint(R.string.route_default)));

        // TODO check that other sections aren't displayed yet.

        // Click route section.
        onView(withId(R.id.alarmSettings_route)).perform(click());

        // Type new route.
        onView(withId(R.id.alarm_route)).perform(replaceText("Or"));

        // Click on auto-suggested route.
        onView(withText("Orange Line"))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());

        // Confirm the route selection is saved.
        intent = activityRule.getActivity().getIntent();
        activityRule.launchActivity(intent);
        onView(withId(R.id.alarmSettings_section_value_route))
                .check(matches(withText("Orange Line")));
    }
}
