package com.amadana.dao;

import java.util.List;
import java.util.Map;

public interface BaseMapper<T> {
    List<T> findAll();
    int update(T t);
    int delete(Integer id);
    int save(T t);
}
