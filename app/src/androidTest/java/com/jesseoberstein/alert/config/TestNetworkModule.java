package com.jesseoberstein.alert.config;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jesseoberstein.alert.network.FakeHttpStack;
import com.jesseoberstein.alert.network.FakeUrlBuilder;
import com.jesseoberstein.alert.network.UrlBuilder;
import com.jesseoberstein.alert.utils.FileHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.testing.TestInstallIn;

@Module
@TestInstallIn(
        components = SingletonComponent.class,
        replaces = NetworkModule.class
)
public class TestNetworkModule {

    @Singleton
    @Provides
    RequestQueue volleyRequestQueue(Application application, FileHelper fileHelper) {
        return Volley.newRequestQueue(application, new FakeHttpStack(fileHelper));
    }

    @Singleton
    @Provides
    UrlBuilder urlBuilder() {
        return new FakeUrlBuilder();
    }
}