package com.jesseoberstein.alert.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.activities.alarm.EditAlarm;
import com.jesseoberstein.alert.databinding.TimeSettingsBinding;
import com.jesseoberstein.alert.interfaces.OnAlarmSubmit;
import com.jesseoberstein.alert.models.UserAlarm;

public class TimeSettingsFragment extends AlarmBaseFragment implements OnAlarmSubmit {

    public static TimeSettingsFragment newInstance(int page) {
        return (TimeSettingsFragment) AlarmBaseFragment.newInstance(page, new TimeSettingsFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        UserAlarm newAlarm = ((EditAlarm) getActivity()).getDraftAlarm();

        TimeSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_alarm_settings_time, container, false);
        binding.setAlarm(newAlarm);

        View view = binding.getRoot();
        view.findViewById(R.id.alarmSettings_time).setOnClickListener(this::showTimePickerDialog);
        view.findViewById(R.id.alarmSettings_name).setOnClickListener(this::showNicknameDialog);

        return view;
    }

    @Override
    public void onAlarmSubmit() {}

    /**
     * Show a dialog with a time picker.
     */
    private void showTimePickerDialog(View view) {
        SetTimeFragment timePickerDialog = new SetTimeFragment();
        timePickerDialog.show(getActivity().getSupportFragmentManager(), "timePicker");
    }

    /**
     * Show a dialog where the user can set an alarm nickname.
     */
    private void showNicknameDialog(View view) {
        SetNicknameFragment setNicknameDialog = new SetNicknameFragment();
        setNicknameDialog.show(getActivity().getSupportFragmentManager(), "setNickname");
    }
}
