package com.jesseoberstein.alert.data;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.jesseoberstein.alert.data.database.AppDatabase;

import java.util.Optional;

@VisibleForTesting
public abstract class AppDatabaseTest extends AppDatabase {
    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        return Optional.ofNullable(instance).orElseGet(() -> {
            instance = Room.inMemoryDatabaseBuilder(context, AppDatabase.class)
                    .addCallback(buildCreateDbCallback(context))
                    .build();
            return instance;
        });
    }

    public static void clear() {
        instance = null;
    }
}
