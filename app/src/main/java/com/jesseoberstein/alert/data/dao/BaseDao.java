package com.jesseoberstein.alert.data.dao;

import java.util.Collection;
import java.util.List;

public interface BaseDao<T> {

    List<T> getAll();

    void insert(T[] elements);

    void delete(T[] elements);

    long count();
}
