package com.jesseoberstein.alert.config;

import android.app.Application;

import androidx.room.Room;

import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.utils.DatabaseCallbackBuilder;
import com.jesseoberstein.alert.utils.FileHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.android.testing.HiltTestApplication;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.testing.TestInstallIn;

@Module
@TestInstallIn(
    components = SingletonComponent.class,
    replaces = DatabaseModule.class
)
public class TestDatabaseModule {

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
        return Room.inMemoryDatabaseBuilder(application.getApplicationContext(), AppDatabase.class)
                .fallbackToDestructiveMigration()
                .addCallback(databaseCallbackBuilder.buildCreateDbCallback())
                .build();
    }
}