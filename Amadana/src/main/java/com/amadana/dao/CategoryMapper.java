package com.amadana.dao;

import com.amadana.entity.Category;

import java.util.List;

public interface CategoryMapper extends BaseMapper {

    List<Category> findCategoryByName(String categoryName);
}
