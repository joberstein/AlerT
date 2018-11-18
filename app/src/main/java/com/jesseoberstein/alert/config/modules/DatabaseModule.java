package com.jesseoberstein.alert.config.modules;

import android.arch.persistence.room.Room;

import com.jesseoberstein.alert.MainApplication;
import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.utils.DatabaseCallbackBuilder;
import com.jesseoberstein.alert.utils.FileHelper;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;

import static com.jesseoberstein.alert.data.database.AppDatabase.ALERT_DATABASE_NAME;

@Module
public class DatabaseModule {

    @Reusable
    @Provides
    FileHelper fileHelper(MainApplication application) {
        return new FileHelper(application);
    }

    @Reusable
    @Provides
    DatabaseCallbackBuilder databaseCallbackBuilder (FileHelper fileHelper) {
        return new DatabaseCallbackBuilder(fileHelper);
    }

    @Reusable
    @Provides
    AppDatabase database(MainApplication application, DatabaseCallbackBuilder databaseCallbackBuilder) {
        return Room.databaseBuilder(application, AppDatabase.class, ALERT_DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .addCallback(databaseCallbackBuilder.buildCreateDbCallback())
                .build();
    }
}
