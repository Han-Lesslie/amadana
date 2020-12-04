package com.amadana.dao;

import com.amadana.entity.Product;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface ProductMapper extends BaseMapper {
    /**
     * 根据id查出所有的product和相关的category及产品详情
     * @param id
     * @return
     */
    List<Product> findByProductId(Integer id);

    List<Product> search(Map<String,Object> map);
}
