package com.amadana.serviceImpl;

import com.amadana.dao.CategoryMapper;
import com.amadana.entity.Category;
import com.amadana.service.CategoryService;
import com.amadana.utils.DateFormat;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public PageInfo findByPage(int currentPage, int pageSize) {
        currentPage = currentPage <= 0 ? 1 : currentPage;
        pageSize = pageSize <= 0 ? 5 : pageSize;
        PageHelper.startPage(currentPage, pageSize);

        List<Category> categories = categoryMapper.findAll();
        PageInfo pageInfo = new PageInfo(categories);
        return pageInfo;
    }

    @Override
    public boolean deleteCategory(Integer id) {
        if (null == id) {
            return false;
        }
        try{
            int count = categoryMapper.delete(id);
            return count == 0 ? false : true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateCategory(Category category) {
        if (null == category || null == category.getId()) {
            return false;
        }
        try{
            category.setUpdateTime(DateFormat.dateFormat(new Date()));
            int count = categoryMapper.update(category);
            return count == 0 ? false : true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean saveCategory(Category category) {
        if (null == category) {
            return false;
        }
        try{
            category.setCreateTime(DateFormat.dateFormat(new Date()));
            category.setStatus(1);
            int count = categoryMapper.save(category);
            return count == 0 ? false : true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Category> findAll() {
        return categoryMapper.findAll();
    }
}
