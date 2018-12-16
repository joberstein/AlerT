package com.jesseoberstein.alert;

import com.jesseoberstein.alert.config.components.DaggerApplicationComponent;

import dagger.android.AndroidInjector;

public class MainApplication extends AbstractApplication {

    @Override
    protected AndroidInjector<MainApplication> applicationInjector() {
        return DaggerApplicationComponent.builder().create(this);
    }
}