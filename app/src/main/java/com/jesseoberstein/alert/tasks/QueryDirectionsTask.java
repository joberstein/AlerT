package com.jesseoberstein.alert.tasks;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.jesseoberstein.alert.data.dao.DirectionDao;
import com.jesseoberstein.alert.fragments.dialog.alarm.SetDirectionDialog;
import com.jesseoberstein.alert.models.mbta.Direction;

import java.util.ArrayList;
import java.util.List;

import static com.jesseoberstein.alert.utils.Constants.DIRECTIONS;

public class QueryDirectionsTask extends AsyncTask<String, Void, List<Direction>> {
    private final DirectionDao directionDao;
    private final FragmentManager fragmentManager;

    public QueryDirectionsTask(DirectionDao directionDao, FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        this.directionDao = directionDao;
    }

    @Override
    protected List<Direction> doInBackground(String[] routeIds) {
        return directionDao.get(routeIds);
    }

    @Override
    protected void onPostExecute(List<Direction> directions) {
        SetDirectionDialog setDirectionDialog = new SetDirectionDialog();
        Bundle directionsBundle = new Bundle(1);
        directionsBundle.putParcelableArrayList(DIRECTIONS, new ArrayList<>(directions));
        setDirectionDialog.setArguments(directionsBundle);
        setDirectionDialog.show(fragmentManager, "setDirectionId");
    }
}
