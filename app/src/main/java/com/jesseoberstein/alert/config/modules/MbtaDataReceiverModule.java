package com.jesseoberstein.alert.config.modules;

import android.content.Context;

import com.jesseoberstein.alert.interfaces.data.DirectionsReceiver;
import com.jesseoberstein.alert.interfaces.data.EndpointsReceiver;
import com.jesseoberstein.alert.interfaces.data.RoutesReceiver;
import com.jesseoberstein.alert.interfaces.data.StopsReceiver;

import dagger.Module;
import dagger.Provides;

@Module
public class MbtaDataReceiverModule {

    @Provides
    RoutesReceiver routesReceiver(Context context) {
        return (RoutesReceiver) context;
    }

    @Provides
    DirectionsReceiver directionsReceiver(Context context) {
        return (DirectionsReceiver) context;
    }

    @Provides
    EndpointsReceiver endpointsReceiver(Context context) {
        return (EndpointsReceiver) context;
    }

    @Provides
    StopsReceiver stopsReceiver(Context context) {
        return (StopsReceiver) context;
    }
}
