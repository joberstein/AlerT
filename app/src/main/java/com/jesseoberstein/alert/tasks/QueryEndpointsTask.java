package com.jesseoberstein.alert.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.jesseoberstein.alert.data.dao.EndpointDao;
import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.interfaces.data.EndpointsReceiver;
import com.jesseoberstein.alert.models.mbta.Endpoint;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class QueryEndpointsTask extends AsyncTask<String, Void, List<Endpoint>> {
    private final EndpointDao endpointDao;
    private final EndpointsReceiver endpointsReceiver;

    @Inject
    public QueryEndpointsTask(Context context, AppDatabase appDatabase) {
        this.endpointDao = appDatabase.endpointDao();
        this.endpointsReceiver = (EndpointsReceiver) context;
    }

    @Override
    protected List<Endpoint> doInBackground(String[] params) {
        if (params.length != 2) {
            return new ArrayList<>();
        }

        String routeId = params[0];
        int directionId = Integer.parseInt(params[1]);
        return this.endpointDao.get(routeId, directionId);
    }

    @Override
    protected void onPostExecute(List<Endpoint> endpoints) {
        this.endpointsReceiver.onReceiveEndpoints(endpoints);
    }
}