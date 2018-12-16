package com.jesseoberstein.alert;

import com.jesseoberstein.alert.config.DaggerTestApplicationComponent;

import dagger.android.AndroidInjector;

public class TestApplication extends AbstractApplication {

    @Override
    protected AndroidInjector<TestApplication> applicationInjector() {
        return DaggerTestApplicationComponent.builder().create(this);
    }
}