package com.jesseoberstein.alert.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.jesseoberstein.alert.data.dao.StopDao;
import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.interfaces.data.StopsReceiver;
import com.jesseoberstein.alert.models.mbta.Stop;

import java.util.List;

public class QueryStopsTask extends AsyncTask<String, Void, List<Stop>> {
    private final StopDao stopDao;
    private final StopsReceiver stopsReceiver;

    public QueryStopsTask(Context context) {
        this.stopDao = AppDatabase.getInstance(context).stopDao();
        this.stopsReceiver = (StopsReceiver) context;
    }

    @Override
    protected List<Stop> doInBackground(String[] routeIds) {
        return stopDao.get(routeIds);
    }

    @Override
    protected void onPostExecute(List<Stop> stops) {
        this.stopsReceiver.onReceiveStops(stops);
    }
}
