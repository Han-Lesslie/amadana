package com.amadana.dao;

import com.amadana.entity.ProductDetail;

import java.util.List;

public interface ProductDetailMapper extends BaseMapper {

    int batchSaveProductDetail(List<ProductDetail> productDetails);

    int deleteDetailById(Integer id);
}
