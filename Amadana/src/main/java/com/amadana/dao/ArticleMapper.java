package com.amadana.dao;

import com.amadana.entity.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleMapper extends BaseMapper {

    int saveArticle(Article article);
    Article getArticleById(Integer id);
    Article getArticleDetailById(Integer id);
    List<Article> getArticles();
}
