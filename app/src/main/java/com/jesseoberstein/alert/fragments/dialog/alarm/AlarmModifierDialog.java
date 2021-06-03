package com.jesseoberstein.alert.fragments.dialog.alarm;


import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.viewmodels.DraftAlarmViewModel;

public abstract class AlarmModifierDialog extends DialogFragment {

    protected UserAlarm userAlarm;
    protected DraftAlarmViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.viewModel = new ViewModelProvider(requireActivity()).get(DraftAlarmViewModel.class);
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.viewModel.getDraftAlarm().observe(requireActivity(), userAlarm -> {
            this.userAlarm = userAlarm;
        });

        return super.onCreateDialog(savedInstanceState);
    }
}
