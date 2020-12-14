package com.amadana.service;

import com.amadana.entity.Product;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface ProductService {

    boolean saveProduct(Product product);

    PageInfo findAdd(int currentPage,int pageSize);

    boolean deleteProuct(Product product);
    List<Product> findByProductId(Integer id);

    PageInfo search(Map<String,Object> map,int currentPage,int pageSize);

    boolean updateProduct(Product product);

    Product getProductById(Integer id);

    List<Product> getProductByCategory(String categoryName);

    Product productDetail(Integer id);
}
