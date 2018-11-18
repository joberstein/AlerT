package com.jesseoberstein.alert.fragments.dialog.alarm;

import android.os.Bundle;

import com.jesseoberstein.alert.fragments.dialog.alarms.RemoveAlarmDialog;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;

import org.junit.Before;
import org.junit.Test;

import static com.jesseoberstein.alert.utils.Constants.ALARM;
import static junit.framework.Assert.assertEquals;

public class RemoveAlarmDialogTest {
    private RemoveAlarmDialog removeAlarmDialog;
    private Bundle bundle;
    private UserAlarm alarm;
    private UserAlarmWithRelations alarmWithRelations;

    @Before
    public void setup() {
        removeAlarmDialog = new RemoveAlarmDialog();
        bundle = new Bundle();
        alarm = new UserAlarm();
        alarmWithRelations =  new UserAlarmWithRelations();

    }

    @Test
    public void getAlarmWithRelations() {
        setDialogArguments();
        assertEquals(alarmWithRelations, removeAlarmDialog.getAlarmWithRelations());
    }

    @Test
    public void getAlarmNickname_nicknameNotPresent() {
        setDialogArguments();
        assertEquals("this alarm", removeAlarmDialog.getAlarmNickname());
    }

    @Test
    public void getAlarmNickname_nicknamePresent() {
        alarm.setNickname("Test");
        setDialogArguments();
        assertEquals("Test", removeAlarmDialog.getAlarmNickname());
    }

    private void setDialogArguments() {
        alarmWithRelations.setAlarm(alarm);
        bundle.putSerializable(ALARM, alarmWithRelations);
        removeAlarmDialog.setArguments(bundle);
    }
}
