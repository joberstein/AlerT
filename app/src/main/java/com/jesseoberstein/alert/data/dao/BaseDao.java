package com.jesseoberstein.alert.data.dao;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import java.util.List;

public interface BaseDao<T> {

    List<T> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(T[] elements);

    @Update
    int update(T[] elements);

    @Delete
    int delete(T[] elements);

    long count();
}
