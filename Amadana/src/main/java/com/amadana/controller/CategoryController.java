package com.amadana.controller;

import com.amadana.annotation.UserLoginToken;
import com.amadana.constant.Constant;
import com.amadana.entity.Category;
import com.amadana.enums.StateCode;
import com.amadana.service.CategoryService;
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

@CrossOrigin(origins = "*", maxAge = 36000)
@Api(tags = "产品分类接口")
@RestController
@RequestMapping("/api")
public class CategoryController {

    private final static Logger LOGER  = LoggerFactory.getLogger(CategoryController.class);
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 获取产品分类分页信息
     * @param currentPage
     * @param pageSize
     * @param request
     * @param response
     * @return
     */
    @ApiOperation("产品分类分页")
    @GetMapping("/getCategory")
    @UserLoginToken
    public ResponseResult getCategory(@ApiParam("当前页")@RequestParam("currentPage")int currentPage, @ApiParam("页数")@RequestParam("pageSize")int pageSize,
                                      HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("token");
        boolean isExpire = isExpire(token);

        if (!isExpire) {
            redisUtils.setExpire(token,token, Constant.EXPIRE_TIME);
            PageInfo pageInfo = categoryService.findByPage(currentPage,pageSize);

            if (pageInfo != null) {
                return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage(),
                        (int) pageInfo.getTotal(),pageInfo.getList());
            }
            return new ResponseResult(StateCode.FAILED.getCode(),StateCode.FAILED.getMessage());
        }
        return new ResponseResult(StateCode.UNAUTHORIZED.getCode(),StateCode.UNAUTHORIZED.getMessage());
    }

    /**
     * 添加产品分类
     * @param category
     * @param request
     * @param response
     * @return
     */
    @ApiOperation("添加产品分类")
    @PostMapping("/saveCategory")
    public ResponseResult saveCategory(@ApiParam("产品分类内容")@RequestBody Category category, HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("token");
        boolean isExpire = isExpire(token);
        if (!isExpire) {
            redisUtils.setExpire(token,token,Constant.EXPIRE_TIME);
            boolean flag = categoryService.saveCategory(category);
            if (flag) {
                return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage());
            }
            return new ResponseResult(StateCode.FAILED.getCode(),StateCode.FAILED.getMessage());
        }
        return new ResponseResult(StateCode.UNAUTHORIZED.getCode(),StateCode.UNAUTHORIZED.getMessage());
    }

    /**
     * 更新产品分类
     * @param category
     * @param request
     * @param response
     * @return
     */
    @ApiOperation("更新产品分类")
    @PostMapping("updateCategory")
    @UserLoginToken
    public ResponseResult updateCategory(@ApiParam("产品分类内容")@RequestBody Category category, HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("token");
        boolean isExpire = isExpire(token);
        if (!isExpire) {
            redisUtils.setExpire(token,token,Constant.EXPIRE_TIME);
            boolean flag = categoryService.updateCategory(category);
            if (flag) {
                return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage());
            }
            return new ResponseResult(StateCode.FAILED.getCode(),StateCode.FAILED.getMessage());
        }
        return new ResponseResult(StateCode.UNAUTHORIZED.getCode(),StateCode.UNAUTHORIZED.getMessage());
    }

    /**
     * 获取所有分类
     * @param request
     * @param response
     * @return
     */
    @ApiOperation("获取所有产品分类")
    @GetMapping("/findAllCategory")
    public ResponseResult findAllCategory(HttpServletRequest request,HttpServletResponse response) {
        String token = request.getHeader("token");
        boolean isExpire = isExpire(token);
        if (!isExpire) {
            redisUtils.setExpire(token,token,Constant.EXPIRE_TIME);
            return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage(),
                    categoryService.findAll());
        }
        return new ResponseResult(StateCode.UNAUTHORIZED.getCode(),StateCode.UNAUTHORIZED.getMessage());
    }
    /**
     * 删除产品分类
     * @param id
     * @param request
     * @param response
     * @return
     */
    @ApiOperation("删除产品分类")
    @GetMapping("/deleteCategory")
    @UserLoginToken
    public ResponseResult deleteCategory(@ApiParam("产品分类ID")@RequestParam("id") Integer id, HttpServletRequest request, HttpServletResponse response) {
        String token  = request.getHeader("token");
        boolean isExpire = isExpire(token);
        if (!isExpire) {
            redisUtils.setExpire(token,token,Constant.EXPIRE_TIME);
            boolean flag = categoryService.deleteCategory(id);
            if (flag) {
                return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage());
            }
            return new ResponseResult(StateCode.FAILED.getCode(),StateCode.FAILED.getMessage());
        }
        return new ResponseResult(StateCode.UNAUTHORIZED.getCode(),StateCode.UNAUTHORIZED.getMessage());
    }

    /**
     * 获取产品分类
     * @return
     */
    @ApiOperation("获取产品分类")
    @GetMapping("/getAllCategory")
    public ResponseResult getAllCategory() {
        return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage(),categoryService.getAllCategory());
    }
    /**
     * 验证token是否过期
     * @param token
     * @return
     */
    private boolean isExpire(String token) {
        String value = (String) redisUtils.get(token);
        return value == null ? true : false;
    }
}
