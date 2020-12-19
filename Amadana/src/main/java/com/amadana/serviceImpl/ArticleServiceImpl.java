package com.amadana.serviceImpl;

import com.amadana.dao.ArticleMapper;
import com.amadana.entity.Article;
import com.amadana.service.ArticleService;
import com.amadana.service.FileUploadService;
import com.amadana.utils.DateFormat;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private FileUploadService fileUploadService;

    @Override
    public boolean saveArticle(Article article) {

        int count = 0;
        if (article.getId() != null) {
            count = articleMapper.update(article);
        }else {
            count = articleMapper.saveArticle(article);
        }
        return count <= 0 ? false : true;
    }

    @Override
    public Article getArticleById(Integer id) {
        if (null != id) {
            return articleMapper.getArticleDetailById(id);
        }
        return null;
    }

    /**
     * 分页信息
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo listArtcileByPage(int currentPage, int pageSize) {
        currentPage = currentPage <= 0 ? 1 : currentPage;
        pageSize = pageSize <= 0 ? 6 : pageSize;
        PageHelper.startPage(currentPage,pageSize);
        List<Article> articleList = articleMapper.findAll();
        // 分页
        PageInfo pageInfo = new PageInfo(articleList);
        return pageInfo;
    }

    @Override
    public List<Article> getAll() {
        return articleMapper.findAll();
    }

    @Override
    public boolean update(Article article) {
        if (article.getId() == null) {
            return false;
        }
        Date updateTime = new Date();
        article.setUpdateDate(DateFormat.dateFormat(updateTime));
        try {
            int count = articleMapper.update(article);
            return count <= 0 ? false : true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        if (null == id) {
            return false;
        }
        try {
            Article article = articleMapper.getArticleDetailById(id);
            fileUploadService.deteleFile(article.getImgName());
            int count = articleMapper.delete(id);
            return count <= 0 ? false : true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Article getArticleDetailById(Integer id) {
        if (null != id) {
            return articleMapper.getArticleDetailById(id);
        }
        return null;
    }

    @Override
    public List<Article> getArticles() {
        List<Article> articles = null;
        try {
            articles = articleMapper.getArticles();
        }catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return (null == articles || 0 == articles.size()) ? new ArrayList<>() : articles;
    }
}

