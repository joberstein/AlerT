package com.jesseoberstein.alert.config;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jesseoberstein.alert.network.UrlBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public final class NetworkModule {

    @Singleton
    @Provides
    RequestQueue volleyRequestQueue(Application application) {
        return Volley.newRequestQueue(application);
    }

    @Singleton
    @Provides
    UrlBuilder urlBuilder() {
        return new UrlBuilder();
    }
}
