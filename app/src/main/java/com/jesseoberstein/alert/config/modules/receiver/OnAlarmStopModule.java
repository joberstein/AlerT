package com.jesseoberstein.alert.config.modules.receiver;

import android.app.Application;

import com.jesseoberstein.alert.config.scopes.BroadcastReceiverScope;
import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.receivers.BaseReceiver;
import com.jesseoberstein.alert.receivers.OnAlarmStop;
import com.jesseoberstein.alert.utils.OnAlarmStopHelper;
import com.jesseoberstein.alert.utils.UserAlarmScheduler;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module(includes = BroadcastReceiverModule.class)
abstract class OnAlarmStopModule {

    @BroadcastReceiverScope
    @Binds
    abstract BaseReceiver onAlarmStop(OnAlarmStop receiver);

    @BroadcastReceiverScope
    @Provides
    static OnAlarmStopHelper onAlarmStopHelper(Application application, UserAlarmScheduler alarmScheduler, AppDatabase db) {
        return new OnAlarmStopHelper(application.getApplicationContext(), alarmScheduler, db);
    }
}
