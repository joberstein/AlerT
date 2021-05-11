package com.jesseoberstein.alert.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.databinding.TimeSettingsBinding;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetDurationDialog;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetNicknameDialog;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetRepeatTypeDialog;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetTimeDialog;
import com.jesseoberstein.alert.interfaces.AlarmModifier;
import com.jesseoberstein.alert.models.UserAlarm;

import javax.inject.Inject;

public class TimeSettingsFragment extends AlarmSettingsFragment {

    @Inject
    FragmentManager fragmentManager;

    public static TimeSettingsFragment newInstance(int page) {
        return (TimeSettingsFragment) AlarmSettingsFragment.newInstance(page, new TimeSettingsFragment());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        UserAlarm newAlarm = ((AlarmModifier) getActivity()).getDraftAlarm();

        TimeSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alarm_settings_tab_time, container, false);
        binding.setAlarm(newAlarm);

        View view = binding.getRoot();
        view.findViewById(R.id.alarmSettings_time).setOnClickListener(this::showTimePickerDialog);
        view.findViewById(R.id.alarmSettings_name).setOnClickListener(this::showNicknameDialog);
        view.findViewById(R.id.alarmSettings_repeat).setOnClickListener(this::showRepeatDialog);
        view.findViewById(R.id.alarmSettings_duration).setOnClickListener(this::showDurationDialog);

        return view;
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

    private void showDialogFragment(DialogFragment dialog, String tagName) {
        dialog.show(this.fragmentManager, tagName);
    }
}
