package com.jesseoberstein.alert.suite;

import com.jesseoberstein.alert.activity.editAlarm.AlarmDurationTest;
import com.jesseoberstein.alert.activity.editAlarm.AlarmNameTest;
import com.jesseoberstein.alert.activity.editAlarm.AlarmRepeatTest;
import com.jesseoberstein.alert.activity.editAlarm.AlarmRouteTest;
import com.jesseoberstein.alert.activity.editAlarm.AlarmStopTest;
import com.jesseoberstein.alert.activity.editAlarm.AlarmTimeTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    AlarmTimeTest.class,
    AlarmNameTest.class,
    AlarmDurationTest.class,
    AlarmRepeatTest.class,
    AlarmRouteTest.class,
    AlarmStopTest.class
})
public class EditAlarmTestSuite {}
