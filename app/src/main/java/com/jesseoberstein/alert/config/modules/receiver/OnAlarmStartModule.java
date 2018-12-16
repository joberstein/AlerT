package com.jesseoberstein.alert.config.modules.receiver;

import com.jesseoberstein.alert.config.scopes.BroadcastReceiverScope;
import com.jesseoberstein.alert.receivers.BaseReceiver;
import com.jesseoberstein.alert.receivers.OnAlarmStart;

import dagger.Binds;
import dagger.Module;

@Module(includes = BroadcastReceiverModule.class)
abstract class OnAlarmStartModule {

    @BroadcastReceiverScope
    @Binds
    abstract BaseReceiver onAlarmStart(OnAlarmStart receiver);
}
