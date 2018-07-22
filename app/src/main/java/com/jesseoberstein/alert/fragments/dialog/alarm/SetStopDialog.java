package com.jesseoberstein.alert.fragments.dialog.alarm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.databinding.AlarmStopBinding;
import com.jesseoberstein.alert.interfaces.AlarmStopSetter;
import com.jesseoberstein.alert.interfaces.data.StopsReceiver;
import com.jesseoberstein.alert.models.AutoComplete;
import com.jesseoberstein.alert.models.mbta.Stop;
import com.jesseoberstein.alert.utils.AlertUtils;

import java.util.List;

import static com.jesseoberstein.alert.utils.Constants.DELAY_DIALOG_DISMISS;

/**
 * A dialog fragment that shows a dialog for selecting the alarm stop.
 */
public class SetStopDialog extends AlarmModifierDialog {
    private AlarmStopSetter alarmStopSetter;
    private StopsReceiver stopsReceiver;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.alarmStopSetter = (AlarmStopSetter) getAlarmModifier();
            this.stopsReceiver = (StopsReceiver) context;
        } catch (ClassCastException e) {
            String msg = context + " must implement both AlarmStopSetter and StopsReceiver";
            throw new ClassCastException(msg);
        }
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        List<Stop> stops = this.stopsReceiver.getStopList();
        AutoComplete<Stop> autoComplete = new AutoComplete<>(stops, this::onAutoCompleteItemSelected);
        autoComplete.attachAdapter(getActivity());

        AlarmStopBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_alarm_dialog_stop, null, false);
        binding.setAlarm(getDraftAlarmWithRelations());
        binding.setAutocomplete(autoComplete);

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.stop_dialog_title)
                .setView(binding.getRoot())
                .setOnKeyListener(this::onKeyPressed)
                .create();

        AlertUtils.showKeyboard(dialog.getWindow());
        return dialog;
    }

    void onAutoCompleteItemSelected(AdapterView adapterView, View view, int i, long l) {
        Stop selectedStop = (Stop) adapterView.getItemAtPosition(i);
        this.alarmStopSetter.onAlarmStopSet(selectedStop);
        new android.os.Handler().postDelayed(this::dismiss, DELAY_DIALOG_DISMISS);
    }

    boolean onKeyPressed(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            dialogInterface.dismiss();
        }

        return true;
    }
}
