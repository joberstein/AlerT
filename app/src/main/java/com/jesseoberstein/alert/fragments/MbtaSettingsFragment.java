package com.jesseoberstein.alert.fragments;


import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.databinding.MbtaSettingsBinding;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetDirectionDialog;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetEndpointsDialog;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetRouteDialog;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetStopDialog;
import com.jesseoberstein.alert.interfaces.AlarmModifier;
import com.jesseoberstein.alert.models.mbta.Endpoint;

import java.util.List;

import static java.util.stream.Collectors.joining;

public class MbtaSettingsFragment extends AlarmSettingsFragment {
    private FragmentManager fragmentManager;
    private static final String SELECTED_ENDPOINTS_DELIMITER = ",  ";

    public static MbtaSettingsFragment newInstance(int page) {
        return (MbtaSettingsFragment) AlarmSettingsFragment.newInstance(page, new MbtaSettingsFragment());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AlarmModifier alarmModifier = ((AlarmModifier) getActivity());
        this.fragmentManager = getActivity().getSupportFragmentManager();

        MbtaSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alarm_settings_tab_mbta, container, false);
        binding.setAlarm(alarmModifier.getDraftAlarmWithRelations());

        View view = binding.getRoot();
        view.findViewById(R.id.alarmSettings_route).setOnClickListener(this::showRouteDialog);
        view.findViewById(R.id.alarmSettings_direction).setOnClickListener(this::showDirectionDialog);
        view.findViewById(R.id.alarmSettings_stop).setOnClickListener(this::showStopDialog);
        view.findViewById(R.id.alarmSettings_endpoints).setOnClickListener(this::showEndpointsDialog);

        return view;
    }

    private void showRouteDialog(View view) {
        this.showDialogFragment(new SetRouteDialog(), "setRoute");
    }

    private void showDirectionDialog(View view) {
        this.showDialogFragment(new SetDirectionDialog(), "setDirection");
    }

    private void showStopDialog(View view) {
        this.showDialogFragment(new SetStopDialog(), "setStop");
    }

    private void showEndpointsDialog(View view) {
        this.showDialogFragment(new SetEndpointsDialog(), "setEndpoints");
    }

    private void showDialogFragment(DialogFragment dialog, String tagName) {
        dialog.show(this.fragmentManager, tagName);
    }

    @BindingAdapter("android:text")
    public static void convertEndpointsToString(TextView textView, List<Endpoint> endpoints) {
        String endpointString = endpoints.stream().map(Endpoint::toString).collect(joining(SELECTED_ENDPOINTS_DELIMITER));
        textView.setText(endpointString);
    }
}
