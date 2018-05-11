package com.jesseoberstein.alert.data.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.jesseoberstein.alert.data.dao.DirectionDao;
import com.jesseoberstein.alert.data.dao.EndpointDao;
import com.jesseoberstein.alert.data.dao.RouteDao;
import com.jesseoberstein.alert.data.dao.StopDao;
import com.jesseoberstein.alert.data.dao.UserAlarmDao;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.mbta.Direction;
import com.jesseoberstein.alert.models.mbta.Endpoint;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.models.mbta.Stop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Database(entities = {UserAlarm.class, Route.class, Stop.class, Direction.class, Endpoint.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String ALERT_DATABASE_NAME = "alert";
    private static AppDatabase instance;

    public abstract UserAlarmDao userAlarmDao();
    public abstract RouteDao routeDao();
    public abstract StopDao stopDao();
    public abstract DirectionDao directionDao();
    public abstract EndpointDao endpointDao();

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, ALERT_DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(buildCallback(context))
                    .build();
        }
        return instance;
    }

    @VisibleForTesting
    static Callback buildCallback(Context context) {
        return new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                readMbtaDbFile(context).forEach(db::execSQL);
            }
        };
    }

    /**
     * Read the SQL dump in {@link MbtaDatabase#MBTA_DATABASE_FILENAME} asset file.
     * Contains a sequence of 'create' and 'insert' statements.
     * @return A list of SQL statements to execute that will create & populate the mbta data tables.
     */
    private static List<String> readMbtaDbFile(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(MbtaDatabase.MBTA_DATABASE_FILENAME);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            List<String> content = reader.lines().collect(Collectors.toList());
            reader.close();
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
}
