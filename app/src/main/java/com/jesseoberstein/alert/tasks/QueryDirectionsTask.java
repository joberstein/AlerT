package com.jesseoberstein.alert.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.jesseoberstein.alert.data.dao.DirectionDao;
import com.jesseoberstein.alert.data.database.AppDatabase;
import com.jesseoberstein.alert.interfaces.data.DirectionsReceiver;
import com.jesseoberstein.alert.models.mbta.Direction;

import java.util.List;

public class QueryDirectionsTask extends AsyncTask<String, Void, List<Direction>> {
    private final DirectionDao directionDao;
    private final DirectionsReceiver directionsReceiver;

    public QueryDirectionsTask(Context context, AppDatabase database) {
        this.directionDao = database.directionDao();
        this.directionsReceiver = (DirectionsReceiver) context;
    }

    @Override
    protected List<Direction> doInBackground(String[] routeIds) {
        return directionDao.get(routeIds);
    }

    @Override
    protected void onPostExecute(List<Direction> directions) {
        this.directionsReceiver.onReceiveDirections(directions);
    }
}
