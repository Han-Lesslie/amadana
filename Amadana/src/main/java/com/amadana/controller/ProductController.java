package com.amadana.controller;

import com.amadana.annotation.UserLoginToken;
import com.amadana.constant.Constant;
import com.amadana.dao.ProductDetailMapper;
import com.amadana.entity.Product;
import com.amadana.enums.StateCode;
import com.amadana.service.ProductService;
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
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 36000)
@Api(tags = "产品接口")
@RestController
@RequestMapping("/api")
public class ProductController {

    private final static Logger LOGGER  = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDetailMapper productDetailMapper;
    @Autowired
    private RedisUtils redisUtils;

    @ApiOperation("保存产品")
    @PostMapping("/saveProduct")
    @UserLoginToken
    public ResponseResult saveProduct(@ApiParam("产品信息") @RequestBody Product product, HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("token");
        boolean isExpire = isExpire(token);
        LOGGER.info("product ==================== >>> {}",product);

        if (!isExpire) {
            redisUtils.setExpire(token,token, Constant.EXPIRE_TIME);
            boolean flag = productService.saveProduct(product);
            if (flag) {
                return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage());
            }else {
                return new ResponseResult(StateCode.FAILED.getCode(),StateCode.FAILED.getMessage());
            }
        }else {
            return new ResponseResult(StateCode.UNAUTHORIZED.getCode(),StateCode.UNAUTHORIZED.getMessage());
        }
    }

    @ApiOperation("更新产品")
    @PostMapping("/updateProduct")
    @UserLoginToken
    public ResponseResult updateProduct(@ApiParam("产品信息") @RequestBody Product product, HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("token");
        boolean isExpire = isExpire(token);

        if (!isExpire) {
            redisUtils.setExpire(token,token, Constant.EXPIRE_TIME);
            boolean flag = productService.updateProduct(product);
            if (flag) {
                return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage());
            }else {
                return new ResponseResult(StateCode.FAILED.getCode(),StateCode.FAILED.getMessage());
            }
        }else {
            return new ResponseResult(StateCode.UNAUTHORIZED.getCode(),StateCode.UNAUTHORIZED.getMessage());
        }
    }

    @ApiOperation("产品分页")
    @GetMapping("/findProduct")
    @UserLoginToken
    public ResponseResult findProduct(@ApiParam("当前页")@RequestParam("currentPage")int currentPage,@ApiParam("页数")@RequestParam("pageSize")int pageSize,
                                      HttpServletRequest request,HttpServletResponse response) {
        String token = request.getHeader("token");
        boolean isExpire = isExpire(token);

        if (!isExpire) {
            redisUtils.setExpire(token,token,Constant.EXPIRE_TIME);
            PageInfo pageInfo = productService.findAdd(currentPage,pageSize);

            if (null == pageInfo) {
                return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage());
            }else {
                return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage(), (int) pageInfo.getTotal(),pageInfo.getList());
            }
        }else {
            return new ResponseResult(StateCode.UNAUTHORIZED.getCode(),StateCode.UNAUTHORIZED.getMessage());
        }
    }

    @ApiOperation("删除产品")
    @PostMapping("/deleteProduct")
    public ResponseResult deletProduct(@ApiParam("产品信息")@RequestBody Product product,HttpServletRequest request,HttpServletResponse response) {
        String token  = request.getHeader("token");
        boolean isExpire = isExpire(token);

        if(!isExpire) {
            redisUtils.setExpire(token,token,Constant.EXPIRE_TIME);
            boolean result = productService.deleteProuct(product);
            if (result) {
                return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage());
            }else {
                return new ResponseResult(StateCode.FAILED.getCode(),StateCode.FAILED.getMessage());
            }
        }else {
            return new ResponseResult(StateCode.UNAUTHORIZED.getCode(),StateCode.UNAUTHORIZED.getMessage());
        }
    }

    @ApiOperation("根据ID查找产品")
    @GetMapping("/findByProductId")
    @UserLoginToken
    public ResponseResult findByProductId(@ApiParam("产品ID")@RequestParam("id") Integer id,HttpServletRequest request,HttpServletResponse response) {
        String token = request.getHeader("token");
        boolean isExpire = isExpire(token);

        if (!isExpire) {
            redisUtils.setExpire(token,token,Constant.EXPIRE_TIME);
            List<Product> products = productService.findByProductId(id);
            return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage(),products);
        }else {
            return new ResponseResult(StateCode.UNAUTHORIZED.getCode(),StateCode.UNAUTHORIZED.getMessage());
        }
    }

    @ApiOperation("根据ID查找产品")
    @GetMapping("/getProductById")
    public ResponseResult getProductById(@ApiParam("产品ID")@RequestParam("id") Integer id,HttpServletRequest request,HttpServletResponse response) {
        String token = request.getHeader("token");
        boolean isExpire = isExpire(token);
        if (!isExpire) {
            redisUtils.setExpire(token,token,Constant.EXPIRE_TIME);
            Product product = productService.getProductById(id);
            return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage(),product);
        }else {
            return new ResponseResult(StateCode.UNAUTHORIZED.getCode(),StateCode.UNAUTHORIZED.getMessage());
        }
    }

    @ApiOperation("产品搜索")
    @PostMapping("/searchProduct")
    @UserLoginToken
    public ResponseResult searchProduct(@ApiParam("产品相关信息")@RequestBody  Map map,HttpServletRequest request,HttpServletResponse response) {
        String token = request.getHeader("token");
        boolean isExpire = isExpire(token);
        if (!isExpire) {
            redisUtils.setExpire(token,token,Constant.EXPIRE_TIME);
            int currentPage = (int) map.get("currentPage");
            int pageSize = (int) map.get("pageSize");

            PageInfo pageInfo = productService.search(map,currentPage,pageSize);
            return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage(), (int) pageInfo.getTotal(),pageInfo.getList());
        }else {
            return new ResponseResult(StateCode.UNAUTHORIZED.getCode(),StateCode.UNAUTHORIZED.getMessage());
        }
    }

    @ApiOperation("根据ID删除详情")
    @GetMapping("/deleteDetailById")
    @UserLoginToken
    public ResponseResult deleteDetail(@ApiParam("产品ID")@RequestParam("id") Integer id,HttpServletRequest request,HttpServletResponse response) {
        String token = request.getHeader("token");
        boolean isExpire = isExpire(token);
        System.out.println(id);
        if (!isExpire) {
            redisUtils.setExpire(token,token,Constant.EXPIRE_TIME);
            return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage(),productDetailMapper.deleteDetailById(id));
        }else {
            return new ResponseResult(StateCode.UNAUTHORIZED.getCode(),StateCode.UNAUTHORIZED.getMessage());
        }
    }

    @GetMapping("/getProductByCategory")
    @ApiOperation("根据分类名称获取产品")
    public ResponseResult getProductByCategory(@ApiParam("分类名称")@RequestParam("categoryName") String categoryName){
        return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage(),productService.getProductByCategory(categoryName));
    }
    @GetMapping("/productDetail")
    @ApiOperation("获取产品详情")
    public ResponseResult getProductByCategory(@ApiParam("ID")@RequestParam("id") Integer id){
        return new ResponseResult(StateCode.SUCCESS.getCode(),StateCode.SUCCESS.getMessage(),productService.productDetail(id));
    }
    private boolean isExpire(String token) {
        String value = (String) redisUtils.get(token);
        return value == null ? true : false;
    }
}
