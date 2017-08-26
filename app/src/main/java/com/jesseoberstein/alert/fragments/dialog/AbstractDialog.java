package com.jesseoberstein.alert.fragments.dialog;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.interfaces.OnDialogClick;
import com.jesseoberstein.alert.models.UserAlarm;

import java.util.Optional;

import static android.app.AlertDialog.Builder;
import static com.jesseoberstein.alert.utils.Constants.ALARM;
import static com.jesseoberstein.alert.utils.Constants.NICKNAME;
import static com.jesseoberstein.alert.utils.Constants.ROUTE;

public abstract class AbstractDialog extends DialogFragment {
    private OnDialogClick clickListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            clickListener = (OnDialogClick) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnDialogClick");
        }
    }

    protected Builder createRouteDialogBuilder(Bundle savedInstanceState) {
        return new Builder(getActivity())
                .setNegativeButton(R.string.cancel, (dialog, id) ->
                        clickListener.onCancelSelected(getArguments()));
    }

    protected String getRouteName() {
        return getArguments().getString(ROUTE);
    }

    protected UserAlarm getAlarm() {
        return (UserAlarm) getArguments().getSerializable(ALARM);
    }

    protected String getExistingAlarmName() {
        return getArguments().getString(NICKNAME);
    }

    protected OnDialogClick getClickListener() {
        return this.clickListener;
    }
}
