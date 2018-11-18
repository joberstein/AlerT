package com.jesseoberstein.alert.config;

import android.arch.persistence.room.Room;

import com.jesseoberstein.alert.TestApplication;
import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.utils.DatabaseCallbackBuilder;
import com.jesseoberstein.alert.utils.FileHelper;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;

@Module
class TestDatabaseModule {

    @Reusable
    @Provides
    FileHelper fileHelper(TestApplication application) {
        return new FileHelper(application);
    }

    @Reusable
    @Provides
    DatabaseCallbackBuilder databaseCallbackBuilder(FileHelper fileHelper) {
        return new DatabaseCallbackBuilder(fileHelper);
    }

    @Reusable
    @Provides
    AppDatabase database(TestApplication application, DatabaseCallbackBuilder databaseCallbackBuilder) {
        return Room.inMemoryDatabaseBuilder(application.getApplicationContext(), AppDatabase.class)
                .fallbackToDestructiveMigration()
                .addCallback(databaseCallbackBuilder.buildCreateDbCallback())
                .build();
    }
}