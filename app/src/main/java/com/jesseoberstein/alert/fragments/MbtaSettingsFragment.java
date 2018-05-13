package com.jesseoberstein.alert.fragments;


import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.activities.alarm.EditAlarm;
import com.jesseoberstein.alert.databinding.MbtaSettingsBinding;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetDirectionDialog;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetEndpointsDialog;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetRouteDialog;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetStopDialog;
import com.jesseoberstein.alert.interfaces.OnAlarmSubmit;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.mbta.Endpoint;

import java.util.List;

import static java.util.stream.Collectors.joining;

public class MbtaSettingsFragment extends AlarmSettingsFragment implements OnAlarmSubmit {
    private FragmentManager fragmentManager;

    public static MbtaSettingsFragment newInstance(int page) {
        return (MbtaSettingsFragment) AlarmSettingsFragment.newInstance(page, new MbtaSettingsFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        UserAlarm newAlarm = ((EditAlarm) getActivity()).getDraftAlarm();
        this.fragmentManager = getActivity().getSupportFragmentManager();

        MbtaSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alarm_settings_tab_mbta, container, false);
        binding.setAlarm(newAlarm);

        View view = binding.getRoot();
        view.findViewById(R.id.alarmSettings_route).setOnClickListener(this::showRouteDialog);
        view.findViewById(R.id.alarmSettings_stop).setOnClickListener(this::showStopDialog);
        view.findViewById(R.id.alarmSettings_direction).setOnClickListener(this::showDirectionDialog);
        view.findViewById(R.id.alarmSettings_endpoints).setOnClickListener(this::showEndpointsDialog);

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

    private void showDirectionDialog(View view) {
        this.showDialogFragment(new SetDirectionDialog(), "setDirection");
    }

    private void showEndpointsDialog(View view) {
        this.showDialogFragment(new SetEndpointsDialog(), "setEndpoints");
    }

    private void showDialogFragment(DialogFragment dialog, String tagName) {
        dialog.show(this.fragmentManager, tagName);
    }

    @BindingAdapter("android:text")
    public static void convertEndpointsToString(TextView textView, List<Endpoint> endpoints) {
        String endpointString = endpoints.stream().map(Endpoint::toString).collect(joining(",  "));
        textView.setText(endpointString);
    }
}
