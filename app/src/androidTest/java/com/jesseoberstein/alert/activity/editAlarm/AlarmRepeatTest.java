package com.jesseoberstein.alert.activity.editAlarm;

import com.jesseoberstein.alert.R;

import org.junit.Test;

import java.util.Arrays;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class AlarmRepeatTest extends BaseEditAlarmTest {
    private static final String DEFAULT_REPEAT = "Never";
    private static final String SELECTED_REPEAT = "Custom";
    private static final String[] SELECTED_DAYS = {"Monday", "Wednesday", "Saturday"};

    @Test
    public void repeatSectionLabelAndValue() throws InterruptedException {
        moveToTimeSettingsTab();
        confirmRepeatLabelAndDefaultValue();

        openRepeatDialog();
        selectRepeat(SELECTED_REPEAT);
        selectDays(SELECTED_DAYS);

        confirmRepeatSelected(SELECTED_REPEAT);
        confirmDaysSelected(SELECTED_DAYS);
    }

    private void openRepeatDialog() {
        openSectionDialog(R.id.alarmSettings_repeat);
    }

    private void selectRepeat(String repeat) throws InterruptedException {
        selectItem(repeat);
    }

    private void selectDays(String... days) throws InterruptedException {
        selectItems(days);
    }

    private void confirmRepeatSelected(String repeat) throws InterruptedException {
        // Check that the repeat string is correct.
        onView(withId(R.id.alarmSettings_section_value_repeat)).check(matches(withText(repeat)));
        openRepeatDialog();

        // Check that the repeat selection persists in the dialog.
        onView(withText(repeat)).check(matches(isChecked()));
        selectRepeat(repeat);
    }

    private void confirmDaysSelected(String... days) {
        Arrays.stream(days).forEach(day -> onView(withText(day)).check(matches(isChecked())));
    }

    private void confirmRepeatLabelAndDefaultValue() {
        confirmSectionLabelAndDefaultValue(
            R.id.alarmSettings_section_label_repeat, R.string.repeat,
            R.id.alarmSettings_section_value_repeat, DEFAULT_REPEAT
        );
    }
}
