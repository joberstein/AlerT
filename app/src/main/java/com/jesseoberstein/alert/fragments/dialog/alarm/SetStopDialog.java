package com.jesseoberstein.alert.fragments.dialog.alarm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.databinding.AlarmStopBinding;
import com.jesseoberstein.alert.models.AutoComplete;
import com.jesseoberstein.alert.models.mbta.Stop;
import com.jesseoberstein.alert.utils.ActivityUtils;
import com.jesseoberstein.alert.utils.LiveDataUtils;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;

import static com.jesseoberstein.alert.utils.Constants.DELAY_DIALOG_DISMISS;

/**
 * A dialog fragment that shows a dialog for selecting the alarm stop.
 */
@AllArgsConstructor
public class SetStopDialog extends AlarmModifierDialog {

    private final List<Stop> stops;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AutoComplete<Stop> autoComplete = new AutoComplete<>(stops, this::onAutoCompleteItemSelected);
        autoComplete.attachAdapter(getActivity());

        AlarmStopBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_alarm_dialog_stop, null, false);
        EditText editText = binding.getRoot().findViewById(R.id.alarm_stop);

        Optional.ofNullable(this.viewModel.getStop().getValue()).ifPresent(stop -> {
            binding.setStop(stop);
            editText.setText(stop.toString());
            editText.setSelection(stop.toString().length());
        });

        binding.setAutocomplete(autoComplete);

        AlertDialog dialog = this.getAlertDialogBuilder()
                .setTitle(R.string.stop_dialog_title)
                .setView(binding.getRoot())
                .setOnKeyListener(this::onKeyPressed)
                .create();

        ActivityUtils.showKeyboard(dialog.getWindow());
        return dialog;
    }

    void onAutoCompleteItemSelected(AdapterView adapterView, View view, int i, long l) {
        Stop selectedStop = (Stop) adapterView.getItemAtPosition(i);
        this.viewModel.getStop().setValue(selectedStop);

        new android.os.Handler().postDelayed(this::dismiss, DELAY_DIALOG_DISMISS);
    }

    boolean onKeyPressed(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            dialogInterface.dismiss();
        }

        return true;
    }
}
