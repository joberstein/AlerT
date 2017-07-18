package com.jesseoberstein.alert.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jesseoberstein.alert.R;

public class DayFragment extends AlarmBaseFragment {

    public static DayFragment newInstance(int page) {
        return (DayFragment) AlarmBaseFragment.newInstance(page, new DayFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_day, container, false);
        TextView stepText = (TextView) view.findViewById(R.id.stepText);
        stepText.setText(R.string.step_2);
        return view;
    }
}
