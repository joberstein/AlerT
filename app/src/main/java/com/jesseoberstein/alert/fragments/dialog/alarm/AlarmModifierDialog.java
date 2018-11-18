package com.jesseoberstein.alert.fragments.dialog.alarm;


import android.content.Context;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;

import com.jesseoberstein.alert.activities.base.BaseDialogFragment;
import com.jesseoberstein.alert.interfaces.AlarmModifier;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;

import java.util.Optional;

import javax.inject.Inject;

public abstract class AlarmModifierDialog extends BaseDialogFragment {

    @Inject
    AlarmModifier alarmModifier;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Optional.ofNullable(getValidationSnackbar()).ifPresent(BaseTransientBottomBar::dismiss);
    }

    UserAlarm getDraftAlarm() {
        return this.alarmModifier.getDraftAlarm();
    }

    UserAlarmWithRelations getDraftAlarmWithRelations() {
        return this.alarmModifier.getDraftAlarmWithRelations();
    }

    Snackbar getValidationSnackbar() {
        return this.alarmModifier.getValidationSnackbar();
    }
}
