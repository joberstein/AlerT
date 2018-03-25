package com.jesseoberstein.alert.fragments.dialog.alarm;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.interfaces.AlarmDurationSetter;
import com.jesseoberstein.alert.interfaces.AlarmRepeatSetter;
import com.jesseoberstein.alert.models.RepeatType;
import com.jesseoberstein.alert.utils.DateTimeUtils;

import java.util.Arrays;
import java.util.List;

import static com.jesseoberstein.alert.models.RepeatType.values;

/**
 * A dialog fragment that shows a dialog for setting the alarm duration.
 */
public class SetDurationDialog extends AlarmModifierDialog {
    private AlarmDurationSetter alarmDurationSetter;
    long[] durationList = new long[]{15L, 30L, 45L, 60L};

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.alarmDurationSetter = (AlarmDurationSetter) getAlarmModifier();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AlarmDurationSetter");
        }
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        long currentDuration = this.getDraftAlarm().getDuration();
        int selectedIndex = Arrays.binarySearch(this.durationList, currentDuration);

        String[] formattedDurations = Arrays.stream(this.durationList)
                .mapToObj(DateTimeUtils::getFormattedDuration)
                .toArray(String[]::new);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.duration_dialog_title)
                .setSingleChoiceItems(formattedDurations, selectedIndex, this::onItemSelected)
                .create();
    }

    private void onItemSelected(DialogInterface dialogInterface, int selectedIndex) {
        this.alarmDurationSetter.onAlarmDurationSet(this.durationList[selectedIndex]);
        new android.os.Handler().postDelayed(dialogInterface::dismiss,500);
    }
}
