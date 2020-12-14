package com.amadana.service;

import com.amadana.entity.Category;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 产品类别接口
 */
public interface CategoryService {

    PageInfo findByPage(int currentPage,int pageSize);
    boolean deleteCategory(Integer id);
    boolean updateCategory(Category category);
    boolean saveCategory(Category category);
    List<Category> findAll();
    List<Category> getAllCategory();
}
