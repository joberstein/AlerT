package com.jesseoberstein.alert.config.modules.service;

import com.android.volley.RequestQueue;
import com.jesseoberstein.alert.config.scopes.ServiceScope;
import com.jesseoberstein.alert.network.UrlBuilder;
import com.jesseoberstein.alert.services.AbstractService;
import com.jesseoberstein.alert.services.MbtaRealtimeUpdatesService;
import com.jesseoberstein.alert.utils.DateTimeHelper;
import com.jesseoberstein.alert.utils.MbtaRealtimeUpdatesHelper;
import com.jesseoberstein.alert.utils.NotificationManagerHelper;
import com.jesseoberstein.alert.utils.UserAlarmScheduler;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module(includes = ServiceModule.class)
abstract class MbtaRealtimeUpdatesServiceModule {

    @ServiceScope
    @Binds
    abstract AbstractService mbtaRealtimeUpdatesService(MbtaRealtimeUpdatesService service);

    @ServiceScope
    @Provides
    static MbtaRealtimeUpdatesHelper mbtaRealtimeUpdatesHelper(
            MbtaRealtimeUpdatesService service,
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
