package com.jesseoberstein.alert.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.activities.alarm.EditAlarm;
import com.jesseoberstein.alert.interfaces.OnAlarmSubmit;
import com.jesseoberstein.alert.models.CustomListItem;
import com.jesseoberstein.alert.models.UserAlarm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MbtaSettingsFragment extends AlarmBaseFragment implements OnAlarmSubmit {
    UserAlarm alarm;

    public static MbtaSettingsFragment newInstance(int page) {
        return (MbtaSettingsFragment) AlarmBaseFragment.newInstance(page, new MbtaSettingsFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alarm_settings_mbta, container, false);
    }

    @Override
    public void onAlarmSubmit() {}
}
