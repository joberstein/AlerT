package com.jesseoberstein.alert.config.modules.service;

import android.content.Context;

import com.jesseoberstein.alert.config.modules.MbtaDataReceiverModule;
import com.jesseoberstein.alert.config.modules.UserAlarmModule;
import com.jesseoberstein.alert.config.scopes.ServiceScope;
import com.jesseoberstein.alert.services.AbstractService;

import dagger.Binds;
import dagger.Module;

@Module(includes = {
    MbtaDataReceiverModule.class,
    UserAlarmModule.class
})
abstract class ServiceModule {

    @ServiceScope
    @Binds
    abstract Context serviceContext(AbstractService service);
}
