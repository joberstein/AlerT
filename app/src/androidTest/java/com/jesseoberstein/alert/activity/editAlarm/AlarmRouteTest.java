package com.jesseoberstein.alert.activity.editAlarm;

import android.support.test.espresso.matcher.RootMatchers;

import com.jesseoberstein.alert.R;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

public class AlarmRouteTest extends BaseEditAlarmTest {

    @Test
    public void routeSectionLabelAndValue() throws InterruptedException {
        moveToMbtaSettingsTab();
        AlarmRouteTest.selectRoute("Or", "Orange Line");
        relaunchActivity();
        confirmRouteSelected("Orange Line");
    }

    @Test
    public void doesNotShowOtherMbtaSectionsIfNotSelected() {
        moveToMbtaSettingsTab();
        onView(withId(R.id.alarmSettings_stop)).check(matches(not(isDisplayed())));
        AlarmRouteTest.selectRoute("Or", "Orange Line");
        relaunchActivity();
        confirmRouteSelected("Orange Line");
        onView(withId(R.id.alarmSettings_stop)).check(matches(isDisplayed()));
    }

    static void selectRoute(String input, String routeName) {
        // Check route label.
        onView(withId(R.id.alarmSettings_section_label_route))
                .check(matches(withText(R.string.route)));

        // Check default route value.
        onView(withId(R.id.alarmSettings_section_value_route))
                .check(matches(withHint(R.string.route_default)));

        // Click route section.
        onView(withId(R.id.alarmSettings_route)).perform(click());

        // Type new route.
        onView(withId(R.id.alarm_route)).perform(replaceText(input));

        // Click on auto-suggested route.
        onView(withText(routeName))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
    }

    private void confirmRouteSelected(String routeName) {
        onView(withId(R.id.alarmSettings_section_value_route))
                .check(matches(withText(routeName)));
    }
}
