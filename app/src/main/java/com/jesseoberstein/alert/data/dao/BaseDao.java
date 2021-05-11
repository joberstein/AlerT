package com.jesseoberstein.alert.data.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

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
