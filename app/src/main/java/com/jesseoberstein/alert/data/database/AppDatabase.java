package com.jesseoberstein.alert.data.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.jesseoberstein.alert.data.dao.AlarmEndpointDao;
import com.jesseoberstein.alert.data.dao.DirectionDao;
import com.jesseoberstein.alert.data.dao.EndpointDao;
import com.jesseoberstein.alert.data.dao.RouteDao;
import com.jesseoberstein.alert.data.dao.StopDao;
import com.jesseoberstein.alert.data.dao.UserAlarmDao;
import com.jesseoberstein.alert.models.AlarmEndpoint;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Database(
    entities = {Route.class, Stop.class, Direction.class, Endpoint.class, UserAlarm.class, AlarmEndpoint.class},
    version = 1
)
public abstract class AppDatabase extends RoomDatabase {
    private static final String ALERT_DATABASE_NAME = "alert";
    private static AppDatabase instance;

    public abstract RouteDao routeDao();
    public abstract StopDao stopDao();
    public abstract DirectionDao directionDao();
    public abstract EndpointDao endpointDao();
    public abstract UserAlarmDao userAlarmDao();
    public abstract AlarmEndpointDao alarmEndpointDao();

    public static AppDatabase getInstance(Context context) {
        return Optional.ofNullable(instance).orElseGet(() -> {
            instance = Room.databaseBuilder(context, AppDatabase.class, ALERT_DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(buildCreateDbCallback(context))
                    .build();
            return instance;
        });
    }

    /**
     * Read in a SQL file that will pre-populate the tables from the MBTA database.
     * @param context The app context.
     * @return The callback that Room should execute when the database is created.
     */
    protected static Callback buildCreateDbCallback(Context context) {
        return new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                readFile(context, MbtaDatabase.MBTA_DATABASE_FILENAME).forEach(db::execSQL);
            }
        };
    }

    /**
     * Read in the the given asset file.
     * @param filename the name of the asset file, including extenstion.
     * @return A list of the lines in the file.
     */
    private static List<String> readFile(Context context, String filename) {
        try {
            InputStream inputStream = context.getAssets().open(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            List<String> content = reader.lines().collect(Collectors.toList());
            reader.close();
            return content;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("File not found", e.getMessage());
        }

        return Collections.emptyList();
    }
}
