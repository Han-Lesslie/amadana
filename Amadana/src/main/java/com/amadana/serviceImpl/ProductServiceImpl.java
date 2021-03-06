package com.amadana.serviceImpl;

import com.amadana.dao.CategoryMapper;
import com.amadana.dao.ProductDetailMapper;
import com.amadana.dao.ProductMapper;
import com.amadana.entity.Category;
import com.amadana.entity.Product;
import com.amadana.entity.ProductDetail;
import com.amadana.service.ProductService;
import com.amadana.utils.CommonUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 产品相关接口服务
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductDetailMapper productDetailMapper;

    private final static Logger LOGGER =  LoggerFactory.getLogger(ProductServiceImpl.class);
    @Override
    public boolean saveProduct(Product product) {
        try {
            List<Category> categories = categoryMapper.findCategoryByName(product.getCategory().getCategoryName());

            if (categories.size() != 0) {
                Category category = categories.get(0);

                product.setCategory(category);
                int count = productMapper.save(product);

                // 插入产品，成功了再插入产品详情
                if (count != 0) {
                    Product pro = new Product();
                    // 设置产品的id
                    pro.setId(product.getId());
                    List<ProductDetail> productDetails = product.getProductDetails();
                    // 设置产品详情url以及对应的产品
                    for (int i=0;i<productDetails.size();i++) {
                        productDetails.get(i).setProduct(pro);
                    }
                    // 批量插入产品详情
                    int result = productDetailMapper.batchSaveProductDetail(productDetails);
                    return result == 0 ? false : true;
                }
            }
            return false;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public PageInfo findProducts(Map<String,Object> map) {
        int currentPage = (int) map.get("currentPage");
        int pageSize = (int) map.get("pageSize");
        currentPage = currentPage <= 0 ? 1 : currentPage;
        pageSize = pageSize <= 0 ? 5 : pageSize;
        PageHelper.startPage(currentPage,pageSize);
        try {
            List<Product> productList = productMapper.search(map);
            if (productList.size() != 0) {
                return new PageInfo(productList);
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    @Override
    public boolean deleteProuct(Product product) {
        if (null == product || null == product.getId()) {
            return false;
        }
        try {
            // 先删除产品详情，然后在删除对应的产品
            int result = productDetailMapper.delete(product.getId());
            if (result != 0) {
                // 开始删除产品
                int count = productMapper.delete(product.getId());
                return count != 0 ? true : false;
            }else {
                return false;
            }
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Product> findByProductId(Integer id) {
        return productMapper.findByProductId(id);
    }

    @Override
    public PageInfo search(Map<String, Object> map, int currentPage, int pageSize) {
        currentPage = currentPage <= 0 ? 1 : currentPage;
        pageSize = pageSize <= 0 ? 5 : pageSize;
        PageHelper.startPage(currentPage,pageSize);
        List<Product> products = productMapper.search(map);
        return new PageInfo(products);
    }

    @Override
    public boolean updateProduct(Product product) {

        try {
            Category category = categoryMapper.findCategoryByName(product.getCategory().getCategoryName()).get(0);
            product.setCategory(category);

            if (null != product && null != product.getId()) {
                LOGGER.info("product:{}",category);

                int count = productMapper.update(product);

                // 更新产品，成功了再插入产品详情
                if (count != 0) {
                    List<ProductDetail> productDetails = product.getProductDetails();
                    int result = 0;
                    // 设置产品详情url以及对应的产品
                    Product pro = new Product();
                    pro.setId(product.getId());
                    for (int i=0;i<productDetails.size();i++) {
                        productDetails.get(i).setProduct(pro);
                        if (productDetails.get(i).getId() != null) {
                            // 批量更新产品详情
                            result = productDetailMapper.update(productDetails.get(i));
                        }else {
                            int finalI = i;
                            result = productDetailMapper.batchSaveProductDetail(new ArrayList<ProductDetail>(){{add(productDetails.get(finalI));}});
                        }
                    }
                    return result == 0 ? false : true;
                }
            }
            return false;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Product getProductById(Integer id) {
        if (null == id) {return  null;}
         try{
             return productMapper.getProductById(id);
         }catch (Exception e) {
             e.printStackTrace();
             return null;
         }
    }

    @Override
    public List<Product> getProductByCategory(String categoryName) {
        Category category  = categoryMapper.findCategoryByName(categoryName).get(0);
        return productMapper.getProductByCategory(category.getId());
    }

    @Override
    public Product productDetail(Integer id) {
        return productMapper.productDetail(id);
    }

    private boolean validateBase64(Product product) {
        if (null == product || null == product.getCategory() || null == product.getCategory().getCategoryName()
                || null == product.getProductDetails()) {
            return true;
        }

        if (CommonUtil.isNull(product.getProductIcon()) || CommonUtil.isNull(product.getProductImg())
                || CommonUtil.isNull(product.getDisplayImg())){
            return true;
        }else{
            //将字符串分成数组
            String[] d = product.getProductIcon().split("base64,");
            String[] d1 = product.getProductImg().split("base64,");
            String[] d2 = product.getDisplayImg().split("base64,");
            if(d == null || d.length != 2) {
                return true;
            }
            if(d1 == null || d1.length != 2) {
                return true;
            }
            if(d2 == null || d2.length != 2) {
                return true;
            }
        }
        return false;
    }
}
