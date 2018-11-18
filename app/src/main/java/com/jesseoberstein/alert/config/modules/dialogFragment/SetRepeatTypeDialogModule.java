package com.jesseoberstein.alert.config.modules.dialogFragment;

import com.jesseoberstein.alert.activities.base.BaseDialogFragment;
import com.jesseoberstein.alert.config.scopes.DialogFragmentScope;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetRepeatTypeDialog;

import dagger.Binds;
import dagger.Module;

@Module(includes = DialogFragmentModule.class)
abstract class SetRepeatTypeDialogModule {

    @DialogFragmentScope
    @Binds
    abstract BaseDialogFragment setRepeatTypeDialog(SetRepeatTypeDialog setRepeatTypeDialog);
}
