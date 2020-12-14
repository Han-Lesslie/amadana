package com.amadana.dao;

import com.amadana.entity.Detail;

import java.util.List;
import java.util.Map;

public interface DetailMapper{

    int save(Detail detail);
    Detail findDetailByName(String name);
    List<Detail> getDetails();
    int updateByName(Map<String, String> map);
    int deleteDetailById(Integer id);
}
