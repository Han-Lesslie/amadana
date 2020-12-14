package com.amadana.service;

import com.amadana.entity.Detail;

import java.util.List;
import java.util.Map;

public interface DetailService {

    boolean saveDetail(Detail detail);

    Detail findDetailByName(String name);

    List<Detail> getDetails();

    boolean updateByName(Map<String,String> map);

    boolean deleteDetailById(Integer id);
}
