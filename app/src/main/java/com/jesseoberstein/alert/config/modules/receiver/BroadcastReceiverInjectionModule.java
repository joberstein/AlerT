package com.jesseoberstein.alert.config.modules.receiver;

import com.jesseoberstein.alert.config.scopes.BroadcastReceiverScope;
import com.jesseoberstein.alert.receivers.OnAlarmStart;
import com.jesseoberstein.alert.receivers.OnAlarmStop;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class BroadcastReceiverInjectionModule {

    @BroadcastReceiverScope
    @ContributesAndroidInjector(modules = OnAlarmStartModule.class)
    abstract OnAlarmStart onAlarmStart();

    @BroadcastReceiverScope
    @ContributesAndroidInjector(modules = OnAlarmStopModule.class)
    abstract OnAlarmStop onAlarmStop();
}
