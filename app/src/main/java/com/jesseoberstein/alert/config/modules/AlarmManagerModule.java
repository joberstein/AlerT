package com.jesseoberstein.alert.config.modules;

import android.app.AlarmManager;
import android.app.Application;
import android.content.Context;

import com.jesseoberstein.alert.utils.AlarmManagerHelper;
import com.jesseoberstein.alert.utils.IntentBuilder;
import com.jesseoberstein.alert.utils.UserAlarmScheduler;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;

@Module
public class AlarmManagerModule {

    @Reusable
    @Provides
    AlarmManager alarmManager(Application application) {
        return (AlarmManager) application.getSystemService(Context.ALARM_SERVICE);
    }

    @Reusable
    @Provides
    IntentBuilder intentBuilder(Context context) {
        return new IntentBuilder(context);
    }

    @Reusable
    @Provides
    AlarmManagerHelper alarmManagerHelper(IntentBuilder intentBuilder,
                                          AlarmManager alarmManager,
                                          UserAlarmScheduler userAlarmScheduler) {
        return new AlarmManagerHelper(intentBuilder, alarmManager, userAlarmScheduler);
    }
}
