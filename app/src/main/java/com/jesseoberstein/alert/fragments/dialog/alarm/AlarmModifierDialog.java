package com.jesseoberstein.alert.fragments.dialog.alarm;


import android.content.Context;
import android.support.v4.app.DialogFragment;

import com.jesseoberstein.alert.interfaces.AlarmModifier;
import com.jesseoberstein.alert.models.UserAlarm;

public abstract class AlarmModifierDialog extends DialogFragment {
    private AlarmModifier alarmModifier;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.alarmModifier = ((AlarmModifier) context);
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
}
