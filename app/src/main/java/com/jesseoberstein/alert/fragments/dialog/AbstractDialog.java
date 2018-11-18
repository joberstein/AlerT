package com.jesseoberstein.alert.fragments.dialog;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.interfaces.OnDialogClick;
import com.jesseoberstein.alert.models.UserAlarmWithRelations;

import java.util.Optional;

import static android.app.AlertDialog.Builder;
import static com.jesseoberstein.alert.utils.Constants.ALARM;

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

    public UserAlarmWithRelations getAlarmWithRelations() {
        return (UserAlarmWithRelations) getArguments().getSerializable(ALARM);
    }

    public String getAlarmNickname() {
        return Optional.ofNullable(getAlarmWithRelations().getAlarm().getNickname())
                .orElse("this alarm");
    }

    protected OnDialogClick getClickListener() {
        return this.clickListener;
    }
}
