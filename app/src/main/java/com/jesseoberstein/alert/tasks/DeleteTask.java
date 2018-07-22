package com.jesseoberstein.alert.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.jesseoberstein.alert.data.dao.BaseDao;

public class DeleteTask<T> extends AsyncTask<T, Void, Integer> {
    private BaseDao<T> dao;
    private String className;

    public DeleteTask(BaseDao<T> dao) {
        this.dao = dao;
        this.className = "";
    }

    @Override
    protected Integer doInBackground(T[] elements) {
        this.className = elements.getClass().getName();
        return this.dao.delete(elements);
    }

    @Override
    protected void onPostExecute(Integer numDeleted) {
        Log.i(this.className, "Deleted " + numDeleted + " elements.");
    }
}
