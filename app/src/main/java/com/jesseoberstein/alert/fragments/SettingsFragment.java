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
import com.jesseoberstein.alert.interfaces.OnAlarmSubmit;
import com.jesseoberstein.alert.listeners.inputs.HideKeyboardOnNextAction;

import static com.jesseoberstein.alert.utils.Constants.DURATION;
import static com.jesseoberstein.alert.utils.Constants.NICKNAME;
import static com.jesseoberstein.alert.utils.Constants.REPEAT;
import static com.jesseoberstein.alert.utils.Constants.STATUS;

public class SettingsFragment extends AlarmBaseFragment implements OnAlarmSubmit {
    private EditText nicknameView;
    private EditText durationView;
    private Spinner repeatView;
    private Switch statusView;

    public static SettingsFragment newInstance(int page) {
        return (SettingsFragment) AlarmBaseFragment.newInstance(page, new SettingsFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_settings, container, false);
        Bundle receivedBundle = getActivity().getIntent().getExtras();

        String nickname = receivedBundle.getString(NICKNAME);
        nicknameView = ((EditText) view.findViewById(R.id.nickname));
        nicknameView.setText(nickname);

        View focusHolder = view.findViewById(R.id.hiddenFocus);
        durationView = ((EditText) view.findViewById(R.id.duration_text));
        durationView.setOnEditorActionListener(new HideKeyboardOnNextAction(focusHolder));

        repeatView = ((Spinner) view.findViewById(R.id.repeat_dropdown));

        boolean status = receivedBundle.getBoolean(STATUS);
        statusView = ((Switch) view.findViewById(R.id.status_toggle));
        statusView.setChecked(status);

        TextView stepText = (TextView) view.findViewById(R.id.stepText);
        stepText.setText(R.string.step_3);

        return view;
    }

    @Override
    public Bundle onAlarmSubmit() {
        Bundle bundle = new Bundle();
        bundle.putString(NICKNAME, nicknameView.getText().toString());
        bundle.putString(DURATION, durationView.getText().toString());
        bundle.putString(REPEAT, repeatView.getSelectedItem().toString());
        bundle.putBoolean(STATUS, statusView.isChecked());
        return bundle;
    }
}
