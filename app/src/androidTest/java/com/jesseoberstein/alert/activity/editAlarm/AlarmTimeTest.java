package com.jesseoberstein.alert.activity.editAlarm;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.utils.DateTimeUtils;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.jesseoberstein.alert.test.TestUtils.setTime;
import static com.jesseoberstein.alert.test.TestUtils.withTime;

public class AlarmTimeTest extends BaseEditAlarmSectionTest {
    private static final int SELECTED_HOUR = 14;
    private static final int SELECTED_MINUTE = 7;

    @Before
    public void prepare() {
        moveToTimeSettingsTab();
        confirmTimeLabelAndDefaultValue();
    }

    @Test
    public void timeSectionLabelAndValue() {
        showTimePicker();
        updateTime(SELECTED_HOUR, SELECTED_MINUTE);
        saveSelectedTime();
        confirmTimeSet("2:07 pm", SELECTED_HOUR, SELECTED_MINUTE);
    }

    private void showTimePicker() {
        openSectionDialog(R.id.alarmSettings_time);
    }

    private void saveSelectedTime() {
        onView(withText(R.string.ok)).perform(click());
    }

    private void updateTime(int hour, int minute) {
        onView(withId(R.id.alarm_time_picker)).perform(setTime(hour, minute));
    }

    private void confirmTimeSet(String time, int hour, int minute) {
        // Check that the nickname string is correct.
        onView(withId(R.id.alarmSettings_section_value_time)).check(matches(withText(time)));
        showTimePicker();

        // Check selected time persists in the dialog.
        onView(withId(R.id.alarm_time_picker)).check(withTime(hour, minute));

        saveSelectedTime();
    }

    private void confirmTimeLabelAndDefaultValue() {
        confirmSectionLabelAndDefaultValue(
            R.id.alarmSettings_section_label_time, R.string.time,
            R.id.alarmSettings_section_value_time, getCurrentTime()
        );
    }

    private String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        int defaultHour = calendar.get(Calendar.HOUR_OF_DAY) + 1;
        int defaultMinute = calendar.get(Calendar.MINUTE);
        return DateTimeUtils.getFormattedTime(defaultHour, defaultMinute);
    }
}
