package com.jesseoberstein.alert.config;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.jesseoberstein.alert.network.FakeHttpStack;
import com.jesseoberstein.alert.network.FakeUrlBuilder;
import com.jesseoberstein.alert.network.UrlBuilder;
import com.jesseoberstein.alert.utils.FileHelper;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;

@Module
class TestNetworkModule {

    @Reusable
    @Provides
    RequestQueue volleyRequestQueue(Application application, FileHelper fileHelper) {
        return Volley.newRequestQueue(application, new FakeHttpStack(fileHelper));
    }

    @Reusable
    @Provides
    UrlBuilder urlBuilder() {
        return new FakeUrlBuilder();
    }
}