package com.jesseoberstein.alert.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.activities.alarm.EditAlarm;
import com.jesseoberstein.alert.interfaces.OnAlarmSubmit;
import com.jesseoberstein.alert.models.CustomListItem;
import com.jesseoberstein.alert.models.UserAlarm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.stream.Collectors;

import static java.util.Calendar.MINUTE;
import static java.util.Calendar.getInstance;

public class TimeSettingsFragment extends AlarmBaseFragment implements OnAlarmSubmit {
    UserAlarm alarm;

    public static TimeSettingsFragment newInstance(int page) {
        return (TimeSettingsFragment) AlarmBaseFragment.newInstance(page, new TimeSettingsFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alarm_settings_time, container, false);
    }

    @Override
    public void onAlarmSubmit() {}
}
