package com.jesseoberstein.alert.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.jesseoberstein.alert.data.dao.BaseDao;

import java.util.Arrays;

public class InsertTask<T> extends AsyncTask<T, Void, Long> {
    private BaseDao<T> dao;
    private String className;

    public InsertTask(BaseDao<T> dao) {
        this.dao = dao;
        this.className = "";
    }

    @Override
    protected Long doInBackground(T[] elements) {
        this.className = elements.getClass().getName();
        long numRecordsBeforeInsert = this.dao.count();
        this.dao.insert(elements);
        long numRecordsAfterInsert = this.dao.count();
        return numRecordsAfterInsert - numRecordsBeforeInsert;
    }

    @Override
    protected void onPostExecute(Long insertCount) {
        Log.i(this.className, "Created " + insertCount + " records.");
    }
}
