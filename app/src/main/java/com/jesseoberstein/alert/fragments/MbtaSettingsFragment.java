package com.jesseoberstein.alert.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.databinding.MbtaSettingsBinding;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetDirectionDialog;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetRouteDialog;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetStopDialog;
import com.jesseoberstein.alert.utils.LiveDataUtils;
import com.jesseoberstein.alert.viewmodels.DraftAlarmViewModel;
import com.jesseoberstein.alert.viewmodels.RoutesViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MbtaSettingsFragment extends AlarmSettingsFragment {

    private DraftAlarmViewModel viewModel;
    private RoutesViewModel routesViewModel;

    public static com.jesseoberstein.alert.fragments.MbtaSettingsFragment newInstance(int page) {
        return (com.jesseoberstein.alert.fragments.MbtaSettingsFragment) AlarmSettingsFragment.newInstance(page, new com.jesseoberstein.alert.fragments.MbtaSettingsFragment());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewModel = new ViewModelProvider(requireActivity()).get(DraftAlarmViewModel.class);
        this.routesViewModel = new ViewModelProvider(requireActivity()).get(RoutesViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MbtaSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alarm_settings_tab_mbta, container, false);
        binding.setLifecycleOwner(requireActivity());
        binding.setViewModel(viewModel);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        view.findViewById(R.id.alarmSettings_route)
                .setOnClickListener(this::showRouteDialog);

        view.findViewById(R.id.alarmSettings_direction)
                .setOnClickListener(this::showDirectionDialog);

        view.findViewById(R.id.alarmSettings_stop)
                .setOnClickListener(this::showStopDialog);
    }

    private void showRouteDialog(View view) {
        this.routesViewModel.loadRoutes();

        LiveDataUtils.observeOnce(requireActivity(), this.routesViewModel.getRoutes(), routes -> {
            this.showDialogFragment(new SetRouteDialog(routes), "setRoute");
        });
    }

    private void showDirectionDialog(View view) {
        this.showDialogFragment(new SetDirectionDialog(), "setDirection");
    }

    private void showStopDialog(View view) {
        this.showDialogFragment(new SetStopDialog(), "setStop");
    }
}