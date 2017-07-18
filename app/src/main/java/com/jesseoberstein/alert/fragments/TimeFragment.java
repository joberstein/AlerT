package com.jesseoberstein.alert.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jesseoberstein.alert.R;

public class TimeFragment extends AlarmBaseFragment {

    public static TimeFragment newInstance(int page) {
        return (TimeFragment) AlarmBaseFragment.newInstance(page, new TimeFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_time, container, false);
        TextView stepText = (TextView) view.findViewById(R.id.stepText);
        stepText.setText(R.string.step_1);
        return view;
    }
}
