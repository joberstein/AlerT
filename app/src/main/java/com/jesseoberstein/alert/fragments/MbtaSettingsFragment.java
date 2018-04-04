package com.jesseoberstein.alert.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.activities.alarm.EditAlarm;
import com.jesseoberstein.alert.databinding.MbtaSettingsBinding;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetRouteDialog;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetStopDialog;
import com.jesseoberstein.alert.interfaces.OnAlarmSubmit;
import com.jesseoberstein.alert.models.UserAlarm;

public class MbtaSettingsFragment extends AlarmSettingsFragment implements OnAlarmSubmit {

    public static MbtaSettingsFragment newInstance(int page) {
        return (MbtaSettingsFragment) AlarmSettingsFragment.newInstance(page, new MbtaSettingsFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        UserAlarm newAlarm = ((EditAlarm) getActivity()).getDraftAlarm();

        MbtaSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alarm_settings_tab_mbta, container, false);
        binding.setAlarm(newAlarm);

        View view = binding.getRoot();
        view.findViewById(R.id.alarmSettings_route).setOnClickListener(this::showRouteDialog);
        view.findViewById(R.id.alarmSettings_stop).setOnClickListener(this::showStopDialog);

        return view;
    }

    @Override
    public void onAlarmSubmit() {}

    private void showRouteDialog(View view) {
        this.showDialogFragment(new SetRouteDialog(), "setRoute");
    }

    private void showStopDialog(View view) {
        this.showDialogFragment(new SetStopDialog(), "setStop");
    }

    private void showDialogFragment(DialogFragment dialog, String tagName) {
        dialog.show(getActivity().getSupportFragmentManager(), tagName);
    }
}
