package com.jesseoberstein.alert.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.activities.alarm.EditAlarm;
import com.jesseoberstein.alert.interfaces.OnAlarmSubmit;
import com.jesseoberstein.alert.listeners.inputs.HideKeyboardOnNextAction;
import com.jesseoberstein.alert.models.UserAlarm;

import java.util.Locale;
import java.util.stream.IntStream;

public class SettingsFragment extends AlarmBaseFragment implements OnAlarmSubmit {
    private UserAlarm alarm;
    private EditText nicknameView;
    private EditText durationView;
    private Spinner durationDropdown;
    private Spinner repeatDropdown;
    private Switch statusView;

    public static SettingsFragment newInstance(int page) {
        return (SettingsFragment) AlarmBaseFragment.newInstance(page, new SettingsFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_settings, container, false);
        alarm = ((EditAlarm) getActivity()).getAlarm();

        TextView stepText = (TextView) view.findViewById(R.id.stepText);
        stepText.setText(R.string.step_3);

        nicknameView = ((EditText) view.findViewById(R.id.nickname));
        nicknameView.setText(alarm.getNickname());

        View focusHolder = view.findViewById(R.id.hiddenFocus);
        durationView = ((EditText) view.findViewById(R.id.duration_text));
        durationView.setOnEditorActionListener(new HideKeyboardOnNextAction(focusHolder));
        durationView.setText(String.format(Locale.US, "%d", alarm.getDuration()));

        durationDropdown = ((Spinner) view.findViewById(R.id.duration_dropdown));
        setSpinnerSelection(durationDropdown, alarm.getDurationType());

        repeatDropdown = ((Spinner) view.findViewById(R.id.repeat_dropdown));
        setSpinnerSelection(repeatDropdown, alarm.getRepeat());

        statusView = ((Switch) view.findViewById(R.id.status_toggle));
        statusView.setChecked(alarm.isActive());

        return view;
    }

    @Override
    public void onAlarmSubmit() {
        alarm.setNickname(nicknameView.getText().toString());
        alarm.setDuration(Integer.valueOf(durationView.getText().toString()));
        alarm.setDurationType(durationDropdown.getSelectedItem().toString());
        alarm.setRepeat(repeatDropdown.getSelectedItem().toString());
        alarm.setActive(statusView.isChecked());
    }

    private void setSpinnerSelection(Spinner spinner, String item) {
        int newPosition = IntStream.range(0, spinner.getCount())
                .filter(i -> spinner.getItemAtPosition(i).toString().equals(item))
                .findFirst()
                .orElse(-1);

        if (newPosition > -1) {
            spinner.setSelection(newPosition);
        }
    }
}
