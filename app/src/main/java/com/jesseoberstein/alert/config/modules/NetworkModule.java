package com.jesseoberstein.alert.config.modules;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jesseoberstein.alert.network.UrlBuilder;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;

@Module
public class NetworkModule {

    @Reusable
    @Provides
    RequestQueue volleyRequestQueue(Application application) {
        return Volley.newRequestQueue(application);
    }

    @Reusable
    @Provides
    UrlBuilder urlBuilder() {
        return new UrlBuilder();
    }
}
