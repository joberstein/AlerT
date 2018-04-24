package com.jesseoberstein.alert.data;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;
import com.jesseoberstein.alert.R;
import com.jesseoberstein.alert.models.UserAlarm;
import com.jesseoberstein.alert.models.mbta.Endpoint;
import com.jesseoberstein.alert.models.mbta.Route;
import com.jesseoberstein.alert.models.mbta.Stop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    public static final String ALERT_DATABASE_NAME = "alert";
    private static final int ALERT_DATABASE_VERSION = 1;

    public static final String MBTA_DATABASE_NAME = "mbta";
    private static final int MBTA_DATABASE_VERSION = 1;
    private static final String MBTA_DATABASE_FILENAME = MBTA_DATABASE_NAME + ".db";

    private static DatabaseHelper instance;
    private static RuntimeExceptionDao<Route, String> routeDao;
    private static RuntimeExceptionDao<Stop, String> stopDao;
    private static RuntimeExceptionDao<Endpoint, Integer> endpointDao;
    private static RuntimeExceptionDao<UserAlarm, Integer> userAlarmDao;

    private AssetManager assetManager;
    private String dbName;

    private DatabaseHelper(Context context, String dbName, int dbVersion) {
        super(context, dbName, null, dbVersion, R.raw.ormlite_config);
        this.dbName = dbName;
        assetManager = context.getAssets();

        routeDao = getInstance(routeDao, getRuntimeExceptionDao(Route.class));
        RouteDao.init();

        stopDao = getInstance(stopDao, getRuntimeExceptionDao(Stop.class));
        StopDao.init();

        endpointDao = getInstance(endpointDao, getRuntimeExceptionDao(Endpoint.class));
        EndpointDao.init();

        userAlarmDao = getInstance(userAlarmDao, getRuntimeExceptionDao(UserAlarm.class));
        UserAlarmDao.init();
    }

    public static void init(Context context, boolean refreshMbtaData) {
        if (instance == null) {
            instance = refreshMbtaData ?
                    new DatabaseHelper(context, MBTA_DATABASE_NAME, MBTA_DATABASE_VERSION) :
                    new DatabaseHelper(context, ALERT_DATABASE_NAME, ALERT_DATABASE_VERSION);
        }
    }

    public static DatabaseHelper get() {
        return instance;
    }

    static RuntimeExceptionDao<Route, String> getRouteDao() {
        return routeDao;
    }

    static RuntimeExceptionDao<Stop, String> getStopDao() {
        return stopDao;
    }

    static RuntimeExceptionDao<Endpoint, Integer> getEndpointDao() {
        return endpointDao;
    }

    static RuntimeExceptionDao<UserAlarm, Integer> getUserAlarmDao() {
        return userAlarmDao;
    }

    @Override
    public void onOpen(SQLiteDatabase database) {
        super.onOpen(database);
        if (!database.isReadOnly()){
            database.setForeignKeyConstraintsEnabled(true);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        // Only need to create the MBTA tables if we're pulling data from the api.
        if (this.dbName.equals(MBTA_DATABASE_NAME)) {
            List<Class<?>> mbtaDaoClasses = Arrays.asList(Route.class, Stop.class, Endpoint.class);
            mbtaDaoClasses.forEach(daoClass -> createTable(connectionSource, daoClass));
            Log.i("DatabaseHelper", "Created 'mbta' database.");
            return;
        }

        this.populateMbtaTables();
        createTable(connectionSource, UserAlarm.class);
        Log.i("DatabaseHelper", "Created 'alert' database.");
    }

    /**
     * 1) Read the {@link #MBTA_DATABASE_FILENAME} asset file.
     * 2) Insert the dump of tables from the mbta database: {@link #MBTA_DATABASE_NAME} into this
     * app's internal database: {@link #ALERT_DATABASE_NAME}.
     */
    private void populateMbtaTables() {
        try {
            DatabaseConnection dbConnection = connectionSource.getReadWriteConnection();
            for (String line : readMbtaDbFile()) {
                dbConnection.executeStatement(line, 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read the SQL dump in {@link #MBTA_DATABASE_FILENAME} asset file.  Contains a sequence of
     * 'create' and 'insert' statements.
     * @return A list of SQL statements to execute that will create & populate the mbta data tables.
     */
    private List<String> readMbtaDbFile() {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(assetManager.open(MBTA_DATABASE_FILENAME));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            List<String> content = reader.lines().collect(Collectors.toList());
            reader.close();
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        if (this.dbName.equals(MBTA_DATABASE_NAME)) {
            List<Class<?>> mbtaDaoClasses = Arrays.asList(Route.class, Stop.class, Endpoint.class);
            mbtaDaoClasses.forEach(daoClass -> dropTable(connectionSource, daoClass));
            this.onCreate(db, connectionSource);
        }
    }

    /**
     * Wrapper for ormlite's {@link TableUtils#createTable}.
     * @param connectionSource
     * @param daoClass
     */
    private void createTable(ConnectionSource connectionSource, Class<?> daoClass) {
        try {
            TableUtils.createTable(connectionSource, daoClass);
            Log.i(daoClass.getName(), "Created table.");
        } catch (SQLException e) {
            Log.e(daoClass.getName(), "Could not create table.");
            throw new RuntimeException(e);
        }
    }

    /**
     * Wrapper for ormlite's {@link TableUtils#dropTable}.
     * @param connectionSource
     * @param daoClass
     */
    private void dropTable(ConnectionSource connectionSource, Class<?> daoClass) {
        try {
            TableUtils.dropTable(connectionSource, daoClass, false);
            Log.i(daoClass.getName(), "Dropped table.");
        } catch (SQLException e) {
            Log.e(daoClass.getName(), "Could not drop table.");
            throw new RuntimeException(e);
        }
    }

    private <T> T getInstance(T instance, T initialValue) {
        if (instance == null) {
            return initialValue;
        }
        return instance;
    }
}
