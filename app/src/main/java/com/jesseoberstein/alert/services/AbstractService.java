package com.jesseoberstein.alert.services;

import dagger.android.DaggerIntentService;

public abstract class AbstractService extends DaggerIntentService {

    public AbstractService(String name) {
        super(name);
    }
}
