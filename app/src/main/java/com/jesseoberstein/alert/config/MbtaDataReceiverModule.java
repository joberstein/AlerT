package com.jesseoberstein.alert.config;

import android.app.Activity;

import com.jesseoberstein.alert.interfaces.data.DirectionsReceiver;
import com.jesseoberstein.alert.interfaces.data.StopsReceiver;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;

@Module
@InstallIn(ActivityComponent.class)
public final class MbtaDataReceiverModule {

    @Provides
    DirectionsReceiver directionsReceiver(Activity activity) {
        return (DirectionsReceiver) activity;
    }

    @Provides
    StopsReceiver stopsReceiver(Activity activity) {
        return (StopsReceiver) activity;
    }
}
