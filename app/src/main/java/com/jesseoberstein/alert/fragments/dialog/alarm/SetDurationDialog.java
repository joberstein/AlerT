package com.jesseoberstein.alert.fragments.dialog.alarm;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.utils.DateTimeHelper;

import java.util.Arrays;

import static com.jesseoberstein.alert.utils.Constants.DELAY_DIALOG_DISMISS;

/**
 * A dialog fragment that shows a dialog for setting the alarm duration.
 */
public class SetDurationDialog extends AlarmModifierDialog {

    final long[] durationList = new long[]{15L, 30L, 45L, 60L};

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        int selectedIndex = Arrays.binarySearch(this.durationList, this.userAlarm.getDuration());

        String[] formattedDurations = Arrays.stream(this.durationList)
                .mapToObj(DateTimeHelper::getFormattedDuration)
                .toArray(String[]::new);

        return this.getAlertDialogBuilder()
                .setTitle(R.string.duration_dialog_title)
                .setSingleChoiceItems(formattedDurations, selectedIndex, this::onItemSelected)
                .create();
    }

    private void onItemSelected(DialogInterface dialogInterface, int selectedIndex) {
        UserAlarm newAlarm = this.userAlarm.withDuration(this.durationList[selectedIndex]);
        this.viewModel.getDraftAlarm().setValue(newAlarm);

        new android.os.Handler().postDelayed(dialogInterface::dismiss, DELAY_DIALOG_DISMISS);
    }
}
