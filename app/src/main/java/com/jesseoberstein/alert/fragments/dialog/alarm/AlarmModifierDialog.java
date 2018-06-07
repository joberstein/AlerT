package com.jesseoberstein.alert.fragments.dialog.alarm;


import android.content.Context;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;

import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.interfaces.AlarmModifier;
import com.jesseoberstein.alert.models.UserAlarm;

import java.util.Optional;

public abstract class AlarmModifierDialog extends DialogFragment {
    private AlarmModifier alarmModifier;
    private AppDatabase db;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.alarmModifier = ((AlarmModifier) context);
            this.db = AppDatabase.getInstance(context);
            Optional.ofNullable(getValidationSnackbar()).ifPresent(BaseTransientBottomBar::dismiss);
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AlarmModifier");
        }
    }

    AlarmModifier getAlarmModifier() {
        return this.alarmModifier;
    }

    UserAlarm getDraftAlarm() {
        return this.alarmModifier.getDraftAlarm();
    }

    Snackbar getValidationSnackbar() {
        return this.alarmModifier.getValidationSnackbar();
    }

    AppDatabase getDatabase() {
        return this.db;
    }
}
