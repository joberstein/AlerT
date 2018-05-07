package com.jesseoberstein.alert.tasks;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.jesseoberstein.alert.data.dao.StopDao;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetStopDialog;
import com.jesseoberstein.alert.models.mbta.Stop;

import java.util.ArrayList;
import java.util.List;

import static com.jesseoberstein.alert.utils.Constants.STOPS;

public class QueryStopsTask extends AsyncTask<String, Void, List<Stop>> {
    private final StopDao stopDao;
    private final FragmentManager fragmentManager;

    public QueryStopsTask(StopDao stopDao, FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        this.stopDao = stopDao;
    }

    @Override
    protected List<Stop> doInBackground(String[] routeIds) {
        return stopDao.get(routeIds);
    }

    @Override
    protected void onPostExecute(List<Stop> stops) {
        SetStopDialog setStopDialog = new SetStopDialog();
        Bundle stopsBundle = new Bundle(1);
        stopsBundle.putParcelableArrayList(STOPS, new ArrayList<>(stops));
        setStopDialog.setArguments(stopsBundle);
        setStopDialog.show(fragmentManager, "setStopId");
    }
}
