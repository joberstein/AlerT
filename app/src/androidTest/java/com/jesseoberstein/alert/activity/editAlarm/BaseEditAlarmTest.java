package com.jesseoberstein.alert.activity.editAlarm;

import android.content.Intent;
import android.support.test.espresso.matcher.RootMatchers;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.activities.alarm.EditAlarm;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import java.util.Arrays;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.jesseoberstein.alert.utils.Constants.DELAY_DIALOG_DISMISS;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

public class BaseEditAlarmTest {
    private Intent intent;

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

    void relaunchActivity() {
        intent = activityRule.getActivity().getIntent();
        activityRule.launchActivity(intent);
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
        String tabDescription = "tab " + Integer.toString(tabIcon);
        Matcher<View> tabView = allOf(
            isDescendantOfA(withId(R.id.alarm_settings_tabs)),
            withChild(withContentDescription(is(tabDescription)))
        );

        onView(tabView).perform(click()).check(matches(isCompletelyDisplayed()));
    }

    static void openSectionDialog(int sectionId) {
        onView(withId(sectionId)).perform(click());
    }

    static void selectItem(String itemName) throws InterruptedException {
        // Click on item with the given item name.
        onView(withText(itemName)).perform(click());

        // Wait until the dialog is dismissed.
        Thread.sleep(DELAY_DIALOG_DISMISS);
    }

    static void selectItems(String... itemNames) throws InterruptedException {
        // Click on item with the given item name.
        Arrays.stream(itemNames).forEach(name -> onView(withText(name)).perform(click()));

        // Save item selection.
        onView(withText(R.string.ok)).perform(click());

        // Wait until the dialog is dismissed.
        Thread.sleep(DELAY_DIALOG_DISMISS);
    }

    static void selectAutosuggestItem(int autosuggestId, String input) throws InterruptedException {
        // Type new item.
        onView(withId(autosuggestId)).perform(replaceText(input.substring(0, 2)));

        // Click on auto-suggested item.
        onView(withText(input)).inRoot(RootMatchers.isPlatformPopup()).perform(click());

        // Wait until the dialog is dismissed.
        Thread.sleep(DELAY_DIALOG_DISMISS);
    }

    void confirmSectionLabel(int labelId, int labelTextId) {
        onView(withId(labelId)).check(matches(withText(labelTextId)));
    }

    void confirmSectionLabelAndDefaultHint(int labelId, int labelTextId, int valueId, int valueHintId) {
        confirmSectionLabel(labelId, labelTextId);

        // Check the section default value hint by id.
        onView(withId(valueId)).check(matches(withHint(valueHintId)));
    }

    void confirmSectionLabelAndDefaultValue(int labelId, int labelTextId, int valueId, String defaultValue) {
        confirmSectionLabel(labelId, labelTextId);

        // Check the section default value text.
        onView(withId(valueId)).check(matches(withText(defaultValue)));
    }
}
