package com.jesseoberstein.alert.activity.editAlarm;

import android.content.Intent;
import android.view.View;

import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.rule.ActivityTestRule;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.TestApplication;
import com.jesseoberstein.alert.activities.alarm.EditAlarm;
import com.jesseoberstein.alert.config.DaggerTestApplicationComponent;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Rule;

import java.util.Arrays;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.jesseoberstein.alert.utils.Constants.DELAY_DIALOG_DISMISS;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

public class BaseEditAlarmSectionTest {

    @Rule
    public ActivityTestRule<EditAlarm> activityRule = new ActivityTestRule<>(EditAlarm.class);

    @After
    public void cleanup() {
        TestApplication app = (TestApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();
        DaggerTestApplicationComponent.builder().create(app).inject(app);
        activityRule.finishActivity();
    }

    void relaunchActivity() {
        Intent intent = activityRule.getActivity().getIntent();
        activityRule.launchActivity(intent);
    }

    public static void moveToTimeSettingsTab() {
        moveToSettingsTab(R.drawable.ic_alarm);
    }

    public static void moveToMbtaSettingsTab() {
        moveToSettingsTab(R.drawable.ic_train);
    }

    void verifyErrorMessageShowsOnSave(int errorMessage) {
        saveAlarm();
        verifyErrorMessageDisplayed(errorMessage);
        onView(withText(R.string.fix)).perform(click());
    }

    void verifyErrorMessageNotShownOnSave(int errorMessage) {
        saveAlarm();
        verifyErrorMessageNotDisplayed(errorMessage);
    }

    void verifyErrorMessageDisplayed(int errorMessage) {
        onView(withText(errorMessage)).check(matches(isDisplayed()));
    }

    void verifyErrorMessageNotDisplayed(int errorMessage) {
        onView(withText(errorMessage)).check(doesNotExist());
    }

    public static void saveAlarm() {
        onView(withContentDescription("Save Alarm")).perform(click());
    }

    private static void moveToSettingsTab(int tabIcon) {
        String tabDescription = "tab " + Integer.toString(tabIcon);
        Matcher<View> tabView = withContentDescription(tabDescription);

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
        onView(withId(autosuggestId)).perform(click(), replaceText(input.substring(0, 2)));

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
