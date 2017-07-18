package com.jesseoberstein.alert.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jesseoberstein.alert.R;

public class SettingsFragment extends AlarmBaseFragment {

    public static SettingsFragment newInstance(int page) {
        return (SettingsFragment) AlarmBaseFragment.newInstance(page, new SettingsFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_settings, container, false);
        TextView stepText = (TextView) view.findViewById(R.id.stepText);
        stepText.setText(R.string.step_3);
        return view;
    }
}
