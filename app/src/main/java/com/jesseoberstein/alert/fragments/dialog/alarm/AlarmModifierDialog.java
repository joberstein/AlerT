package com.jesseoberstein.alert.fragments.dialog.alarm;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.ContextThemeWrapper;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.viewmodels.DraftAlarmViewModel;

import java.util.Optional;

public abstract class AlarmModifierDialog extends DialogFragment {

    protected DraftAlarmViewModel viewModel;
    private int themeId = R.style.AlarmSettingsDark_Default;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.viewModel = new ViewModelProvider(requireActivity()).get(DraftAlarmViewModel.class);
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Optional.ofNullable(this.viewModel.getThemeId().getValue())
                .ifPresent(themeId -> this.themeId = themeId);

        return super.onCreateDialog(savedInstanceState);
    }

    protected AlertDialog.Builder getAlertDialogBuilder() {
        ContextThemeWrapper themeWrapper = new ContextThemeWrapper(requireActivity(), this.themeId);
        return new AlertDialog.Builder(themeWrapper);
    }
}
