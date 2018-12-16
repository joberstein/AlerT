package com.jesseoberstein.alert.config.modules.receiver;

import android.app.Application;
import android.content.Context;

import com.jesseoberstein.alert.config.modules.UserAlarmModule;

import dagger.Binds;
import dagger.Module;

@Module(includes = UserAlarmModule.class)
abstract class BroadcastReceiverModule {

    @Binds
    abstract Context broadcastReceiverContext(Application application);
}
