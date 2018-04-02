package com.jesseoberstein.alert.fragments.dialog.alarm;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.databinding.AlarmNicknameBinding;
import com.jesseoberstein.alert.utils.AlertUtils;

/**
 * A dialog fragment that shows a dialog for editing the alarm nickname.
 */
public class SetNicknameDialog extends AlarmModifierDialog {

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlarmNicknameBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_alarm_dialog_nickname, null, false);
        binding.setAlarm(getDraftAlarm());

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.nickname_dialog_title)
                .setView(binding.getRoot())
                .setOnKeyListener(this::onKeyPressed)
                .create();

        AlertUtils.showKeyboard(dialog.getWindow());
        return dialog;
    }

    public boolean onKeyPressed(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            dialogInterface.dismiss();
        }

        return true;
    }

    @BindingAdapter("isCursorAtEnd")
    public static void setEditTextCursor(EditText editText, boolean isCursorAtEnd) {
        if (isCursorAtEnd) {
            editText.setSelection(editText.getText().length());
        }
    }
}
