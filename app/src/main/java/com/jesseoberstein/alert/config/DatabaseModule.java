package com.jesseoberstein.alert.config;

import android.app.Application;

import androidx.room.Room;

import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.utils.DatabaseCallbackBuilder;
import com.jesseoberstein.alert.utils.FileHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

import static com.jesseoberstein.alert.data.database.AppDatabase.ALERT_DATABASE_NAME;

@Module
@InstallIn(SingletonComponent.class)
public final class DatabaseModule {

    @Singleton
    @Provides
    FileHelper fileHelper(Application application) {
        return new FileHelper(application);
    }

    @Singleton
    @Provides
    DatabaseCallbackBuilder databaseCallbackBuilder(FileHelper fileHelper) {
        return new DatabaseCallbackBuilder(fileHelper);
    }

    @Singleton
    @Provides
    AppDatabase database(Application application, DatabaseCallbackBuilder databaseCallbackBuilder) {
        return Room.databaseBuilder(application, AppDatabase.class, ALERT_DATABASE_NAME)
                .fallbackToDestructiveMigrationFrom(1)
                .addCallback(databaseCallbackBuilder.buildCreateDbCallback())
                .build();
    }
}
