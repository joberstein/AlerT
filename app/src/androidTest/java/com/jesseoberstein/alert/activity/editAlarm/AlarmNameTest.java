package com.jesseoberstein.alert.activity.editAlarm;

import com.jesseoberstein.alert.R;

import org.junit.Before;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class AlarmNameTest extends BaseEditAlarmSectionTest {
    private static final String TEST_NICKNAME = "Test Name";

    @Before
    public void prepare() {
        moveToTimeSettingsTab();
        confirmNameLabelAndDefaultValue();
    }

    @Test
    public void nameSectionLabelAndValue() {
        openNicknameDialog();
        setNickname(TEST_NICKNAME);
        confirmNicknameSet(TEST_NICKNAME);
    }

    private void openNicknameDialog() {
        openSectionDialog(R.id.alarmSettings_name);
    }

    private void setNickname(String nickname) {
        onView(withId(R.id.alarm_nickname))
                .perform(replaceText(nickname))
                .perform(pressImeActionButton());
    }

    private void confirmNicknameSet(String nickname) {
        // Check that the nickname string is correct.
        onView(withId(R.id.alarmSettings_section_value_name)).check(matches(withText(nickname)));
        openNicknameDialog();

        // Check that the nickname selection persists in the dialog.
        onView(withId(R.id.alarm_nickname)).check(matches(withText(nickname)));
        setNickname(nickname);
    }

    private void confirmNameLabelAndDefaultValue() {
        confirmSectionLabelAndDefaultHint(
            R.id.alarmSettings_section_label_name, R.string.nickname,
            R.id.alarmSettings_section_value_name, R.string.nickname_default
        );
    }
}
