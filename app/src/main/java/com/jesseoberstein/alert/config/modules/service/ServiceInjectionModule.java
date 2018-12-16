package com.jesseoberstein.alert.config.modules.service;

import com.jesseoberstein.alert.config.scopes.ServiceScope;
import com.jesseoberstein.alert.services.MbtaRealtimeUpdatesService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ServiceInjectionModule {

    @ServiceScope
    @ContributesAndroidInjector(modules = MbtaRealtimeUpdatesServiceModule.class)
    abstract MbtaRealtimeUpdatesService mbtaRealtimeUpdates();
}
