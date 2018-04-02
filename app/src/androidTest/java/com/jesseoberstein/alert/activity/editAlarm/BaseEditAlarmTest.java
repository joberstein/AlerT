package com.jesseoberstein.alert.activity.editAlarm;

import android.content.Intent;
import android.support.test.espresso.matcher.RootMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.activities.alarm.EditAlarm;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.jesseoberstein.alert.utils.Constants.DELAY_DIALOG_DISMISS;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

public class BaseEditAlarmTest {
    Intent intent;

    @Rule
    public ActivityTestRule<EditAlarm> activityRule;

    @Before
    public void setup() {
        activityRule = new ActivityTestRule<>(EditAlarm.class, true, false);
        intent = new Intent();
    }

    @After
    public void cleanup() {
        activityRule.finishActivity();
    }

    void moveToTimeSettingsTab() {
        activityRule.launchActivity(intent);
        moveToSettingsTab(R.drawable.ic_alarm);
    }

    void moveToMbtaSettingsTab() {
        activityRule.launchActivity(intent);
        moveToSettingsTab(R.drawable.ic_train);
    }

    private void moveToSettingsTab(int tabIcon) {
        String description = "tab " + Integer.toString(tabIcon);
        onView(allOf(
                isDescendantOfA(withId(R.id.alarm_settings_tabs)),
                withChild(withContentDescription(is(description)))))
                .perform(click())
                .check(matches(isCompletelyDisplayed()));
    }
}