package com.jesseoberstein.alert.tasks;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.jesseoberstein.alert.data.dao.RouteDao;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetRouteDialog;
import com.jesseoberstein.alert.models.mbta.Route;

import java.util.ArrayList;
import java.util.List;

import static com.jesseoberstein.alert.utils.Constants.ROUTES;

public class QueryRoutesTask extends AsyncTask<Void, Void, List<Route>> {
    private final RouteDao routeDao;
    private final FragmentManager fragmentManager;

    public QueryRoutesTask(RouteDao routeDao, FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        this.routeDao = routeDao;
    }

    @Override
    protected List<Route> doInBackground(Void... voids) {
        return routeDao.getAll();
    }

    @Override
    protected void onPostExecute(List<Route> routes) {
        SetRouteDialog setRouteDialog = new SetRouteDialog();
        Bundle routesBundle = new Bundle(1);
        routesBundle.putParcelableArrayList(ROUTES, new ArrayList<>(routes));
        setRouteDialog.setArguments(routesBundle);
        setRouteDialog.show(fragmentManager, "setRouteId");
    }
}
