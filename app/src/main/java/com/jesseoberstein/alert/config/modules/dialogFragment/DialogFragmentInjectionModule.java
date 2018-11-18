package com.jesseoberstein.alert.config.modules.dialogFragment;

import com.jesseoberstein.alert.config.scopes.DialogFragmentScope;
import com.jesseoberstein.alert.fragments.dialog.alarm.AlarmModifierDialog;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetDaysDialog;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetDirectionDialog;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetDurationDialog;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetEndpointsDialog;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetNicknameDialog;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetRepeatTypeDialog;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetRouteDialog;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetStopDialog;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetTimeDialog;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class DialogFragmentInjectionModule {

    @DialogFragmentScope
    @ContributesAndroidInjector(modules = AlarmModifierDialogModule.class)
    abstract AlarmModifierDialog alarmModifierDialog();

    @DialogFragmentScope
    @ContributesAndroidInjector(modules = SetTimeDialogModule.class)
    abstract SetTimeDialog setTimeDialog();

    @DialogFragmentScope
    @ContributesAndroidInjector(modules = SetNicknameDialogModule.class)
    abstract SetNicknameDialog setNicknameDialog();

    @DialogFragmentScope
    @ContributesAndroidInjector(modules = SetRepeatTypeDialogModule.class)
    abstract SetRepeatTypeDialog setRepeatTypeDialog();

    @DialogFragmentScope
    @ContributesAndroidInjector(modules = SetDaysDialogModule.class)
    abstract SetDaysDialog setDaysDialog();

    @DialogFragmentScope
    @ContributesAndroidInjector(modules = SetDurationDialogModule.class)
    abstract SetDurationDialog setDurationDialog();

    @DialogFragmentScope
    @ContributesAndroidInjector(modules = SetRouteDialogModule.class)
    abstract SetRouteDialog setRouteDialog();

    @DialogFragmentScope
    @ContributesAndroidInjector(modules = SetDirectionDialogModule.class)
    abstract SetDirectionDialog setDirectionDialog();

    @DialogFragmentScope
    @ContributesAndroidInjector(modules = SetStopDialogModule.class)
    abstract SetStopDialog setStopDialog();

    @DialogFragmentScope
    @ContributesAndroidInjector(modules = SetEndpointsDialogModule.class)
    abstract SetEndpointsDialog setEndpointsDialog();
}
