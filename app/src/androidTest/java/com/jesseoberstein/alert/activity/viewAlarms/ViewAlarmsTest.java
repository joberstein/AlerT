package com.jesseoberstein.alert.activity.viewAlarms;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.activities.alarms.ViewAlarmsMock;
import com.jesseoberstein.alert.activity.editAlarm.AlarmDirectionTest;
import com.jesseoberstein.alert.activity.editAlarm.AlarmEndpointsTest;
import com.jesseoberstein.alert.activity.editAlarm.AlarmRouteTest;
import com.jesseoberstein.alert.activity.editAlarm.AlarmStopTest;
import com.jesseoberstein.alert.activity.editAlarm.BaseEditAlarmSectionTest;
import com.jesseoberstein.alert.data.AppDatabaseTest;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.jesseoberstein.alert.utils.Constants.ALARM_ID;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

public class ViewAlarmsTest {

    @Rule
    public IntentsTestRule<ViewAlarmsMock> intentsTestRule = new IntentsTestRule<>(ViewAlarmsMock.class);

    @After
    public void cleanup() {
        AppDatabaseTest.clear();
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
        onView(withText("Haymarket")).check(matches(isDisplayed()));
    }

    @Test
    public void testEditAlarm() throws InterruptedException {
        clickCreateAlarmButton();
        insertAlarm();
        onView(withText("Haymarket")).perform(click());
        verifyUpdateAlarmIntent();
    }

    private void clickCreateAlarmButton() {
        onView(withId(R.id.add_alarm)).check(matches(isDisplayed())).perform(click());
    }

    private void insertAlarm() throws InterruptedException {
        BaseEditAlarmSectionTest.moveToMbtaSettingsTab();
        AlarmRouteTest.selectRoute("Orange Line");
        AlarmDirectionTest.selectDirection("Northbound");
        AlarmStopTest.selectStop("Haymarket");
        AlarmEndpointsTest.selectEndpoints(new String[]{"Oak Grove"}, new String[]{"Oak Grove"});
        BaseEditAlarmSectionTest.saveAlarm();
    }

    private void verifyCreateAlarmIntent() {
        intended(allOf(
            hasComponent("com.jesseoberstein.alert.activities.alarm.EditAlarmMock"),
            hasAction(is(Intent.ACTION_INSERT)),
            not(hasExtraWithKey(ALARM_ID))
        ));
    }

    private void verifyUpdateAlarmIntent() {
        intended(allOf(
            hasComponent("com.jesseoberstein.alert.activities.alarm.EditAlarm"),
            hasAction(is(Intent.ACTION_EDIT)),
            hasExtra(ALARM_ID, 1L)
        ));
    }
}
