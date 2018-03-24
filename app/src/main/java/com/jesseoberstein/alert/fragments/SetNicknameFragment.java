package com.jesseoberstein.alert.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.databinding.AlarmNicknameBinding;
import com.jesseoberstein.alert.interfaces.AlarmModifier;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.utils.AlertUtils;

/**
 * A dialog fragment that shows a dialog for editing the alarm nickname.
 */
public class SetNicknameFragment extends DialogFragment {
    private UserAlarm draftAlarm;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.draftAlarm = ((AlarmModifier) context).getDraftAlarm();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement AlarmSetter");
        }
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlarmNicknameBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_alarm_nickname, null, false);
        binding.setAlarm(this.draftAlarm);

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.nickname_dialog_title)
                .setView(binding.getRoot())
                .setOnKeyListener(this::onKeyPressed)
                .create();

        AlertUtils.showKeyboard(dialog.getWindow());
        return dialog;
    }

    public boolean onKeyPressed(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.KEYCODE_ENTER:
                dialogInterface.dismiss();
        }

        return true;
    }

    @BindingAdapter("app:isCursorAtEnd")
    public static void setEditTextCursor(EditText editText, boolean isCursorAtEnd) {
        if (isCursorAtEnd) {
            editText.setSelection(editText.getText().length());
        }
    }
}
