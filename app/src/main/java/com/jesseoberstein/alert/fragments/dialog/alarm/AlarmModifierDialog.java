package com.jesseoberstein.alert.fragments.dialog.alarm;


import android.content.Context;
import android.support.v4.app.DialogFragment;

import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.interfaces.AlarmModifier;
import com.jesseoberstein.alert.models.UserAlarm;

public abstract class AlarmModifierDialog extends DialogFragment {
    private AlarmModifier alarmModifier;
    private AppDatabase db;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.alarmModifier = ((AlarmModifier) context);
            this.db = AppDatabase.getInstance(context);
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

    AppDatabase getDatabase() {
        return this.db;
    }
}
