package com.jesseoberstein.alert.tasks.base;

import android.os.AsyncTask;
import android.util.Log;

import com.jesseoberstein.alert.data.dao.BaseDao;

import java.util.List;
import java.util.stream.Collectors;

public class UpdateTask<T> extends AsyncTask<T, Void, Integer> {
    private BaseDao<T> dao;
    private String className;

    protected UpdateTask(BaseDao<T> dao) {
        this.dao = dao;
        this.className = "";
    }

    @Override
    protected Integer doInBackground(T[] elements) {
        this.className = elements.getClass().getName();
        return this.dao.update(elements);
    }

    @Override
    protected void onPostExecute(Integer numUpdated) {
        Log.i(this.className, "Updated " + numUpdated + " element(s).");
    }
}
