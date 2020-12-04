package com.amadana.service;

import com.amadana.entity.Article;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 文章增删改查，分页接口
 */
public interface ArticleService {
    boolean saveArticle(Article article);
    Article getArticleById(Integer id);
    PageInfo listArtcileByPage(int currentPage,int pageSize);
    List<Article> getAll();
    boolean update(Article article);
    boolean delete(Integer id);
}
