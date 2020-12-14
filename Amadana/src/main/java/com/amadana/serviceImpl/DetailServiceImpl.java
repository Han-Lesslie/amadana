package com.amadana.serviceImpl;

import com.amadana.dao.DetailMapper;
import com.amadana.entity.Detail;
import com.amadana.service.DetailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DetailServiceImpl implements DetailService {

    @Autowired
    private DetailMapper detailMapper;

    @Override
    public boolean saveDetail(Detail detail) {
        if (null == detail) {
            return false;
        }
        try{
            int result = detailMapper.save(detail);
            return result != 0 ? true : false;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Detail findDetailByName(String name) {
        if (StringUtils.isEmpty(name) || StringUtils.isBlank(name)) {
            return null;
        }
        try {
            return detailMapper.findDetailByName(name);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Detail> getDetails() {
        return detailMapper.getDetails();
    }

    @Override
    public boolean updateByName(Map<String, String> map) {
        if (null == map) {
            return false;
        }
        try{
            int result = detailMapper.updateByName(map);
            return result != 0 ? true : false;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteDetailById(Integer id) {
       if (id == null) {
           return false;
       }
       try {
           int result = detailMapper.deleteDetailById(id);
           return result != 0 ? true : false;
       }catch (Exception e) {
           e.printStackTrace();
           return false;
       }
    }
}
