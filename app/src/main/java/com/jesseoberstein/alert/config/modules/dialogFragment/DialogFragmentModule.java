package com.jesseoberstein.alert.config.modules.dialogFragment;

import android.content.Context;

import com.jesseoberstein.alert.activities.base.BaseDialogFragment;
import com.jesseoberstein.alert.config.modules.DateTimeModule;
import com.jesseoberstein.alert.config.modules.MbtaDataReceiverModule;
import com.jesseoberstein.alert.config.modules.UserAlarmModule;
import com.jesseoberstein.alert.config.scopes.DialogFragmentScope;

import dagger.Module;
import dagger.Provides;

@Module(includes = {
    DateTimeModule.class,
    MbtaDataReceiverModule.class,
    UserAlarmModule.class
})
public class DialogFragmentModule {

    @DialogFragmentScope
    @Provides
    Context fragmentContext(BaseDialogFragment dialogFragment) {
        return dialogFragment.getContext();
    }
}
