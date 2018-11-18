package com.jesseoberstein.alert.tasks.base;

import android.os.AsyncTask;
import android.util.Log;

import com.jesseoberstein.alert.data.dao.BaseDao;

import java.util.List;
import java.util.stream.Collectors;

public class InsertTask<T> extends AsyncTask<T, Void, List<Long>> {
    private BaseDao<T> dao;
    private String className;

    public InsertTask(BaseDao<T> dao) {
        this.dao = dao;
        this.className = "";
    }

    @Override
    protected List<Long> doInBackground(T[] elements) {
        this.className = elements.getClass().getName();
        return this.dao.insert(elements);
    }

    @Override
    protected void onPostExecute(List<Long> insertedIds) {
        String idString = insertedIds.stream().map(Object::toString).collect(Collectors.joining(","));
        Log.i(this.className, "Inserted ids: " + idString);
    }
}
