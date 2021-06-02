package com.jesseoberstein.alert.config;

import android.app.Service;

import com.android.volley.RequestQueue;
import com.jesseoberstein.alert.network.UrlBuilder;
import com.jesseoberstein.alert.services.MbtaRealtimeUpdatesService;
import com.jesseoberstein.alert.utils.DateTimeHelper;
import com.jesseoberstein.alert.utils.MbtaRealtimeUpdatesHelper;
import com.jesseoberstein.alert.utils.NotificationManagerHelper;
import com.jesseoberstein.alert.utils.UserAlarmScheduler;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ServiceComponent;
import dagger.hilt.android.scopes.ServiceScoped;

@Module
@InstallIn(ServiceComponent.class)
public final class MbtaRealtimeUpdatesServiceModule {

    @ServiceScoped
    @Provides
    static MbtaRealtimeUpdatesHelper mbtaRealtimeUpdatesHelper(
            Service service,
            NotificationManagerHelper notificationManagerHelper,
            UserAlarmScheduler userAlarmScheduler,
            DateTimeHelper dateTimeHelper,
            RequestQueue requestQueue,
            UrlBuilder urlBuilder
    ) {
        return new MbtaRealtimeUpdatesHelper(
                service,
                notificationManagerHelper,
                userAlarmScheduler,
                dateTimeHelper,
                requestQueue,
                urlBuilder
        );
    }
}
