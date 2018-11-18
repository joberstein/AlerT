package com.jesseoberstein.alert.config.modules.dialogFragment;

import com.jesseoberstein.alert.activities.base.BaseDialogFragment;
import com.jesseoberstein.alert.config.scopes.DialogFragmentScope;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetNicknameDialog;

import dagger.Binds;
import dagger.Module;

@Module(includes = DialogFragmentModule.class)
abstract class SetNicknameDialogModule {

    @DialogFragmentScope
    @Binds
    abstract BaseDialogFragment setNicknameDialog(SetNicknameDialog setNicknameDialog);
}
