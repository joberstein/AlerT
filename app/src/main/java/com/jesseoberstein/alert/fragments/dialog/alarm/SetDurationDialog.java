package com.jesseoberstein.alert.fragments.dialog.alarm;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.utils.DateTimeHelper;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.jesseoberstein.alert.utils.Constants.DELAY_DIALOG_DISMISS;

/**
 * A dialog fragment that shows a dialog for setting the alarm duration.
 */
public class SetDurationDialog extends AlarmModifierDialog {

    final Duration[] durationList = new Duration[]{
            Duration.ofMinutes(15),
            Duration.ofMinutes(30),
            Duration.ofMinutes(45),
            Duration.ofHours(1)
    };

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        int durationIndex = Optional.ofNullable(this.viewModel.getDuration().getValue())
                .map(duration -> Arrays.binarySearch(this.durationList, duration))
                .orElse(-1);

        String[] formattedDurations = Arrays.stream(this.durationList)
                .map(duration -> String.format("%d minutes", duration.toMinutes()))
                .toArray(String[]::new);

        return this.getAlertDialogBuilder()
                .setTitle(R.string.duration_dialog_title)
                .setSingleChoiceItems(formattedDurations, durationIndex, this::onItemSelected)
                .create();
    }

    private void onItemSelected(DialogInterface dialogInterface, int selectedIndex) {
        Duration duration = this.durationList[selectedIndex];
        this.viewModel.getDuration().setValue(duration);

        new android.os.Handler().postDelayed(dialogInterface::dismiss, DELAY_DIALOG_DISMISS);
    }
}
