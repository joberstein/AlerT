package com.jesseoberstein.alert.activity.viewAlarms;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.TestApplication;
import com.jesseoberstein.alert.activities.alarms.ViewAlarms;
import com.jesseoberstein.alert.activity.editAlarm.AlarmDirectionTest;
import com.jesseoberstein.alert.activity.editAlarm.AlarmEndpointsTest;
import com.jesseoberstein.alert.activity.editAlarm.AlarmRouteTest;
import com.jesseoberstein.alert.activity.editAlarm.AlarmStopTest;
import com.jesseoberstein.alert.activity.editAlarm.BaseEditAlarmSectionTest;
import com.jesseoberstein.alert.config.DaggerTestApplicationComponent;
import com.jesseoberstein.alert.test.TestUtils;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.jesseoberstein.alert.test.TestUtils.*;
import static com.jesseoberstein.alert.utils.AlertUtils.getHexColor;
import static com.jesseoberstein.alert.utils.AlertUtils.getTextColorForWhiteBackground;
import static com.jesseoberstein.alert.utils.Constants.ALARM;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

public class ViewAlarmsTest {
    private final int SELECTED_COLOR = R.color.commuter;
    private final String SELECTED_ROUTE = "Providence/Stoughton Line";
    private final String SELECTED_DIRECTION = "Outbound";
    private final String SELECTED_STOP = "Hyde Park";
    private final String[] SELECTED_ENDPOINTS = {"Attleboro", "Stoughton"};
    private final String SELECTED_ENDPOINTS_STRING = "Attleboro,  Stoughton";
    private final String[] ALL_ENDPOINTS = {"Attleboro", "Providence", "Stoughton", "Wickford Junction"};

    @Rule
    public IntentsTestRule<ViewAlarms> intentsTestRule = new IntentsTestRule<>(ViewAlarms.class);

    @After
    public void cleanup() {
        TestApplication app = (TestApplication) InstrumentationRegistry.getTargetContext().getApplicationContext();
        DaggerTestApplicationComponent.builder().create(app).inject(app);
    }

    @Test
    public void testNoAlarmsMessage() {
        onView(withId(R.id.add_alarm)).check(matches(isDisplayed()));
        onView(withId(R.id.alarm_list_empty)).check(matches(withText(R.string.alarm_list_empty)));
    }

    @Test
    public void testCreateAlarm() throws InterruptedException {
        clickCreateAlarmButton();
        verifyCreateAlarmIntent();
        insertAlarm();
        onView(withText(SELECTED_STOP)).check(matches(isDisplayed()));
    }

    @Test
    public void testUpdateAlarm() throws InterruptedException {
        clickCreateAlarmButton();
        insertAlarm();
        onView(withText(SELECTED_STOP)).perform(click());
        verifyUpdateAlarmIntent();

        BaseEditAlarmSectionTest.moveToMbtaSettingsTab();
        onView(withId(R.id.alarmSettings_section_value_route)).check(matches(withText(SELECTED_ROUTE)));
        onView(withId(R.id.alarmSettings_section_value_direction)).check(matches(withText(SELECTED_DIRECTION)));
        onView(withId(R.id.alarmSettings_section_value_stop)).check(matches(withText(SELECTED_STOP)));
        onView(withId(R.id.alarmSettings_section_value_endpoints)).check(matches(withText(SELECTED_ENDPOINTS_STRING)));

        String updatedStop = "Route 128";
        AlarmStopTest.selectStop(updatedStop);
        BaseEditAlarmSectionTest.saveAlarm();

        onView(withText(updatedStop)).perform(click());
        BaseEditAlarmSectionTest.moveToMbtaSettingsTab();
        onView(withId(R.id.alarmSettings_section_value_stop)).check(matches(withText(updatedStop)));
    }

    @Test
    public void testRemoveAlarmConfirm() throws InterruptedException {
        clickCreateAlarmButton();
        insertAlarm();
        onView(withText(SELECTED_STOP)).perform(longClick());
        verifyRemoveAlarmDialogDisplayed();
        onView(withText(R.string.remove)).perform(click());
        onView(withText(SELECTED_STOP)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testRemoveAlarmCancel() throws InterruptedException {
        clickCreateAlarmButton();
        insertAlarm();
        onView(withText(SELECTED_STOP)).perform(longClick());
        verifyRemoveAlarmDialogDisplayed();
        onView(withText(R.string.cancel)).perform(click());
        onView(withText(SELECTED_STOP)).check(matches(isDisplayed()));
    }

    @Test
    public void testAlarmStatusToggle() throws InterruptedException {
        clickCreateAlarmButton();
        insertAlarm();
        onView(withContentDescription(R.string.alarm_status))
                .check(matches(hasDrawableColor(SELECTED_COLOR)))
                .perform(click())
                .check(matches(hasDrawableColor(R.color.alarm_off)))
                .perform(click())
                .check(matches(hasDrawableColor(SELECTED_COLOR)));
    }

    private void verifyRemoveAlarmDialogDisplayed() {
        Context context = InstrumentationRegistry.getTargetContext();
        String dialogText = context.getResources().getString(R.string.remove_alarm_dialog, "this alarm");
        onView(withText(dialogText)).check(matches(isDisplayed()));
    }

    private void clickCreateAlarmButton() {
        onView(withId(R.id.add_alarm)).check(matches(isDisplayed())).perform(click());
    }

    private void insertAlarm() throws InterruptedException {
        BaseEditAlarmSectionTest.moveToMbtaSettingsTab();
        AlarmRouteTest.selectRoute(SELECTED_ROUTE);
        AlarmDirectionTest.selectDirection(SELECTED_DIRECTION);
        AlarmStopTest.selectStop(SELECTED_STOP);
        AlarmEndpointsTest.selectEndpoints(SELECTED_ENDPOINTS, ALL_ENDPOINTS);
        BaseEditAlarmSectionTest.saveAlarm();
    }

    private void verifyCreateAlarmIntent() {
        intended(allOf(
            hasComponent("com.jesseoberstein.alert.activities.alarm.EditAlarm"),
            hasAction(is(Intent.ACTION_INSERT)),
            not(hasExtraWithKey(ALARM))
        ));
    }

    private void verifyUpdateAlarmIntent() {
        intended(allOf(
            hasComponent("com.jesseoberstein.alert.activities.alarm.EditAlarm"),
            hasAction(is(Intent.ACTION_EDIT)),
            hasExtraWithKey(ALARM)
        ));
    }
}
