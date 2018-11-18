package com.jesseoberstein.alert.config.modules.dialogFragment;

import com.jesseoberstein.alert.activities.base.BaseDialogFragment;
import com.jesseoberstein.alert.config.scopes.DialogFragmentScope;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetDaysDialog;

import dagger.Binds;
import dagger.Module;

@Module(includes = DialogFragmentModule.class)
abstract class SetDaysDialogModule {

    @DialogFragmentScope
    @Binds
    abstract BaseDialogFragment setDaysDialog(SetDaysDialog setDaysDialog);
}

