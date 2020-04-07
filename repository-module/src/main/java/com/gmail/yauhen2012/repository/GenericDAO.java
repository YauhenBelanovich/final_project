package com.gmail.yauhen2012.repository;

import java.util.List;

public interface GenericDAO<I, T> {

    void add(T entity);

    List<T> findAll();

    void remove(T entity);

    T findById(I id);

    List<T> getObjectsByPage(int startPosition, int itemsByPage);

    void merge(T entity);

}
