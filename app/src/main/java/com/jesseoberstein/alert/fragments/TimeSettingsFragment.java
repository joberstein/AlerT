package com.jesseoberstein.alert.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.activities.alarm.EditAlarm;
import com.jesseoberstein.alert.databinding.TimeSettingsBinding;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetDurationDialog;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetNicknameDialog;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetRepeatTypeDialog;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetTimeDialog;
import com.jesseoberstein.alert.interfaces.OnAlarmSubmit;
import com.jesseoberstein.alert.models.UserAlarm;

public class TimeSettingsFragment extends AlarmSettingsFragment implements OnAlarmSubmit {

    public static TimeSettingsFragment newInstance(int page) {
        return (TimeSettingsFragment) AlarmSettingsFragment.newInstance(page, new TimeSettingsFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        UserAlarm newAlarm = ((EditAlarm) getActivity()).getDraftAlarm();

        TimeSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alarm_settings_time, container, false);
        binding.setAlarm(newAlarm);

        View view = binding.getRoot();
        view.findViewById(R.id.alarmSettings_time).setOnClickListener(this::showTimePickerDialog);
        view.findViewById(R.id.alarmSettings_name).setOnClickListener(this::showNicknameDialog);
        view.findViewById(R.id.alarmSettings_repeat).setOnClickListener(this::showRepeatDialog);
        view.findViewById(R.id.alarmSettings_duration).setOnClickListener(this::showDurationDialog);

        return view;
    }

    @Override
    public void onAlarmSubmit() {}

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
        dialog.show(getActivity().getSupportFragmentManager(), tagName);
    }
}
