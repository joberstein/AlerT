package com.jesseoberstein.alert.fragments.dialog.alarm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.databinding.AlarmStopBinding;
import com.jesseoberstein.alert.interfaces.data.StopsReceiver;
import com.jesseoberstein.alert.models.AutoComplete;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.mbta.Stop;
import com.jesseoberstein.alert.utils.ActivityUtils;

import java.util.List;

import javax.inject.Inject;

import static com.jesseoberstein.alert.utils.Constants.DELAY_DIALOG_DISMISS;

/**
 * A dialog fragment that shows a dialog for selecting the alarm stop.
 */
public class SetStopDialog extends AlarmModifierDialog {

    @Inject
    StopsReceiver stopsReceiver;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        List<Stop> stops = this.stopsReceiver.getStopList();
        AutoComplete<Stop> autoComplete = new AutoComplete<>(stops, this::onAutoCompleteItemSelected);
        autoComplete.attachAdapter(getActivity());

        AlarmStopBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_alarm_dialog_stop, null, false);
        this.viewModel.getStop().observe(requireActivity(), binding::setStop);
        binding.setAutocomplete(autoComplete);

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.stop_dialog_title)
                .setView(binding.getRoot())
                .setOnKeyListener(this::onKeyPressed)
                .create();

        ActivityUtils.showKeyboard(dialog.getWindow());
        return dialog;
    }

    void onAutoCompleteItemSelected(AdapterView adapterView, View view, int i, long l) {
        Stop selectedStop = (Stop) adapterView.getItemAtPosition(i);

        UserAlarm newAlarm = this.userAlarm.toBuilder()
                .stopId(selectedStop.getId())
                .build();

        this.viewModel.getDraftAlarm().setValue(newAlarm);

        new android.os.Handler().postDelayed(this::dismiss, DELAY_DIALOG_DISMISS);
    }

    boolean onKeyPressed(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            dialogInterface.dismiss();
        }

        return true;
    }
}
