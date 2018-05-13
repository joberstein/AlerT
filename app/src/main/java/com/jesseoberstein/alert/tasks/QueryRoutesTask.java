package com.jesseoberstein.alert.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.jesseoberstein.alert.data.dao.RouteDao;
import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.interfaces.data.RoutesReceiver;
import com.jesseoberstein.alert.models.mbta.Route;

import java.util.List;

public class QueryRoutesTask extends AsyncTask<Void, Void, List<Route>> {
    private final RouteDao routeDao;
    private final RoutesReceiver routesReceiver;

    public QueryRoutesTask(Context context) {
        this.routeDao = AppDatabase.getInstance(context).routeDao();
        this.routesReceiver = (RoutesReceiver) context;
    }

    @Override
    protected List<Route> doInBackground(Void... voids) {
        return routeDao.getAll();
    }

    @Override
    protected void onPostExecute(List<Route> routes) {
        this.routesReceiver.onReceiveRoutes(routes);
    }
}
