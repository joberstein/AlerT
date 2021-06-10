package com.jesseoberstein.alert.fragments.dialog.alarm;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.databinding.AlarmNicknameBinding;
import com.jesseoberstein.alert.utils.ActivityUtils;
import com.jesseoberstein.alert.utils.LiveDataUtils;

import java.util.Optional;

/**
 * A dialog fragment that shows a dialog for editing the alarm nickname.
 */
public class SetNicknameDialog extends AlarmModifierDialog {

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlarmNicknameBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fragment_alarm_dialog_nickname, null, false);
        EditText editText = binding.getRoot().findViewById(R.id.alarm_nickname);

        Optional.ofNullable(this.viewModel.getNickname().getValue()).ifPresent(nickname -> {
            binding.setNickname(nickname);
            editText.setText(nickname);
            editText.setSelection(nickname.length());
        });

        AlertDialog dialog = this.getAlertDialogBuilder()
                .setTitle(R.string.nickname_dialog_title)
                .setView(binding.getRoot())
                .setOnKeyListener((dialogInterface, keyCode, event) -> this.onKeyPressed(dialogInterface, keyCode, event, binding.getNickname()))
                .create();

        ActivityUtils.showKeyboard(dialog.getWindow());

        return dialog;
    }

    public boolean onKeyPressed(DialogInterface dialog, int keyCode, KeyEvent keyEvent, String nickname) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            this.viewModel.getNickname().setValue(nickname);
            dialog.dismiss();
        }

        return true;
    }
}
