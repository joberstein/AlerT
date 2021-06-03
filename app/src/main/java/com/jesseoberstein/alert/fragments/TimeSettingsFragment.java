package com.jesseoberstein.alert.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.databinding.TimeSettingsBinding;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetDurationDialog;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetNicknameDialog;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetRepeatTypeDialog;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetTimeDialog;
import com.jesseoberstein.alert.viewmodels.DraftAlarmViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TimeSettingsFragment extends AlarmSettingsFragment {

    private DraftAlarmViewModel viewModel;

    public static TimeSettingsFragment newInstance(int page) {
        return (TimeSettingsFragment) AlarmSettingsFragment.newInstance(page, new TimeSettingsFragment());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.viewModel = new ViewModelProvider(requireActivity()).get(DraftAlarmViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TimeSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alarm_settings_tab_time, container, false);
        binding.setLifecycleOwner(requireActivity());
        binding.setViewModel(this.viewModel);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        view.findViewById(R.id.alarmSettings_time)
                .setOnClickListener(this::showTimePickerDialog);

        view.findViewById(R.id.alarmSettings_name)
                .setOnClickListener(this::showNicknameDialog);

        view.findViewById(R.id.alarmSettings_repeat)
                .setOnClickListener(this::showRepeatDialog);

        view.findViewById(R.id.alarmSettings_duration)
                .setOnClickListener(this::showDurationDialog);
    }

    private void showTimePickerDialog(View view) {
        this.showDialogFragment(new SetTimeDialog(), "setTime");
    }

    private void showNicknameDialog(View view) {
        this.showDialogFragment(new SetNicknameDialog(), "setNickname");
    }

    private void showRepeatDialog(View view) {
        this.showDialogFragment(new SetRepeatTypeDialog(), "setRepeat");
    }

    private void showDurationDialog(View view) {
        this.showDialogFragment(new SetDurationDialog(), "setDuration");
    }
}
