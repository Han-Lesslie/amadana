package com.amadana.controller;

import com.alibaba.fastjson.JSONObject;
import com.amadana.annotation.UserLoginToken;
import com.amadana.constant.Constant;
import com.amadana.entity.Article;
import com.amadana.enums.StateCode;
import com.amadana.service.ArticleService;
import com.amadana.utils.RedisUtils;
import com.amadana.vo.ResponseResult;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api")
@Api(tags = "文章接口")
//设置跨域请求源
@CrossOrigin(origins = "*", maxAge = 36000)
public class ArticleController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ArticleController.class);
    @Autowired
    private ArticleService articleService;
    @Autowired
    private RedisUtils redisUtils;
    /**
     * 发布文章
     * @param article
     * @param request
     * @param response
     * @return
     */
    @ApiOperation("发布文章")
    @PostMapping("/publish")
    @UserLoginToken
    public ResponseResult publishArticle(@ApiParam("文章内容") @RequestBody Article article, HttpServletRequest request, HttpServletResponse response) {

        String token = request.getHeader("token");
        // 判断token是否过期
        boolean isExpire = isExpire(token);
        if (!isExpire) {
            boolean isSuccess = articleService.saveArticle(article);
            redisUtils.setExpire(token,token, Constant.EXPIRE_TIME);
            if (isSuccess) {
                return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage());
            }else {
                return new ResponseResult(StateCode.FAILED.getCode(),StateCode.FAILED.getMessage());
            }
        }else {
            return new ResponseResult(StateCode.UNAUTHORIZED.getCode(),StateCode.UNAUTHORIZED.getMessage());
        }
    }

    /**
     * 根据id获取文章
     * @param id
     * @param request
     * @param response
     * @return
     */
    /*@ApiOperation("根据ID查看文章")
    @GetMapping("/getArticleByid")
    @UserLoginToken
    public ResponseResult getArticleById(@ApiParam("文章ID")@RequestParam("id") Integer id,
                                         HttpServletRequest request, HttpServletResponse response) {

        String token = request.getHeader("token");
        boolean isExpire =  isExpire(token);
        if (!isExpire) {
            Article article = articleService.getArticleById(id);
            redisUtils.setExpire(token,token,Constant.EXPIRE_TIME);
            if (null != article) {
                return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage(),article);
            }else {
                return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage(),null);
            }
        }else {
            return new ResponseResult(StateCode.UNAUTHORIZED.getCode(),StateCode.UNAUTHORIZED.getMessage());
        }
    }*/

    /**
     * 获取分页
     * @param currentPage
     * @param pageSize
     * @param request
     * @param response
     * @return
     */
    @ApiOperation("文章分页")
    @GetMapping("/getPage")
    @UserLoginToken
    public ResponseResult getArticlePage(@ApiParam("当前页")@RequestParam("currentPage") int currentPage, @ApiParam("页数")@RequestParam("pageSize") int pageSize,
                                         HttpServletRequest request, HttpServletResponse response) {

        String token = request.getHeader("token");
        boolean isExpire = isExpire(token);
        if (!isExpire) {
            redisUtils.setExpire(token,token,Constant.EXPIRE_TIME);
            PageInfo pageInfo = articleService.listArtcileByPage(currentPage,pageSize);
            return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage()
            , (int) pageInfo.getTotal(),pageInfo.getList());
        }else {
            return new ResponseResult(StateCode.UNAUTHORIZED.getCode(),StateCode.UNAUTHORIZED.getMessage());
        }
    }

    /**
     * 更新文章
     * @param article
     * @param request
     * @param response
     * @return
     */
    @ApiOperation("更新文章")
    @PostMapping("/updateArticle")
    @UserLoginToken
    public ResponseResult updateArticle(@ApiParam("文章内容")@RequestBody Article article, HttpServletRequest request, HttpServletResponse response) {

        String token = request.getHeader("token");
        boolean isExpire = isExpire(token);
        if (!isExpire) {
            boolean state = articleService.update(article);
            redisUtils.setExpire(token,token,Constant.EXPIRE_TIME);
            if (state) {
                return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage());
            }else {
                return new ResponseResult(StateCode.FAILED.getCode(),StateCode.FAILED.getMessage());
            }
        }else {
            return new ResponseResult(StateCode.UNAUTHORIZED.getCode(),StateCode.UNAUTHORIZED.getMessage());
        }
    }

    /**
     * 删除文章，实际上是更新文章的status为0
     * @param id
     * @param request
     * @param response
     * @return
     */
    @ApiOperation("删除文章")
    @GetMapping("/deleteArticle")
    @UserLoginToken
    public ResponseResult deleteArticle(@ApiParam("文章ID")@RequestParam("id") Integer id, HttpServletRequest request, HttpServletResponse response) {

        String token = request.getHeader("token");
        boolean isExpire = isExpire(token);

        if (!isExpire) {
            boolean state = articleService.delete(id);
            redisUtils.setExpire(token,token,Constant.EXPIRE_TIME);
            if (state) {
                return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage());
            }else {
                return new ResponseResult(StateCode.FAILED.getCode(),StateCode.FAILED.getMessage());
            }
        }else {
            return new ResponseResult(StateCode.UNAUTHORIZED.getCode(),StateCode.UNAUTHORIZED.getMessage());
        }
    }

    @ApiOperation("获取全部文章")
    @GetMapping("/getAll")
    public Object getAll() {
        JSONObject res = new JSONObject();
        List<Article> articles = articleService.getAll();
        System.out.println(articles.size());
        res.put("total",articles.size());
        res.put("data",articles);
        return  res;
    }

    private boolean isExpire(String token) {
        String value = (String) redisUtils.get(token);
        return value == null ? true : false;
    }
}
