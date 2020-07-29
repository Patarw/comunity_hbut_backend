package com.hbut.product.service;


import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.hbut.product.VO.OrderMasterVO;
import com.hbut.product.VO.ProductVO;
import com.hbut.product.dao.OrderDetailRepository;
import com.hbut.product.dao.ProductCategoryRepository;
import com.hbut.product.dao.ProductInfoRepository;
import com.hbut.product.entity.ProductCategory;
import com.hbut.product.entity.ProductInfo;
import com.hbut.product.exception.SellException;
import com.hbut.product.form.CategoryForm;
import com.hbut.product.form.ProductForm;
import com.hbut.product.form.ShopCart;
import enums.ProductStatusEnums;
import enums.ResultEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.IdWorker;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductInfoRepository productInfoRepository;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private HttpServletRequest request;


    /**减库存*/
    public void decreaseStock(ShopCart shopCart){
        ProductInfo productInfo = productInfoRepository.findProductInfoByProductId(shopCart.getProductId());
        if (productInfo == null){
            throw new SellException(ResultEnum.NO_GOODS_MSG);
        }
        if (productInfo.getProductStock() < shopCart.getQuantity()){
            throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
        }
        productInfo.setProductStock(productInfo.getProductStock() - shopCart.getQuantity());
        productInfoRepository.save(productInfo);
    }

    /**减库存*/
    public void addStock(ShopCart shopCart){
        ProductInfo productInfo = productInfoRepository.findProductInfoByProductId(shopCart.getProductId());
        if (productInfo == null){
            throw new SellException(ResultEnum.NO_GOODS_MSG);
        }
        productInfo.setProductStock(productInfo.getProductStock() + shopCart.getQuantity());
        productInfoRepository.save(productInfo);
    }

    /**分页列出所有已上架商品*/
    public Result<PageVO<ProductVO>> listUpProduct(PageArg arg){
        arg.validate();
        Pageable pageable = PageRequest.of(arg.getPageNo() - 1,arg.getPageSize());
        Page<ProductInfo> productInfoPage = productInfoRepository.findProductInfoByProductStatusOrderByUpdateTimeDesc(ProductStatusEnums.UP.getCode(),pageable);

        List<ProductInfo> productInfoList = productInfoPage.getContent();
        List<ProductVO> productVOList = new ArrayList<>();
        for (ProductInfo productInfo : productInfoList){
            ProductVO productVO = new ProductVO();
            BeanUtils.copyProperties(productInfo,productVO);
            ProductCategory productCategory = productCategoryRepository.findProductCategoryByCategoryType(productInfo.getCategoryType());
            productVO.setProductCategory(productCategory);
            productVOList.add(productVO);
        }

        int total = productInfoPage.getTotalPages();
        //构建pageVo对象
        PageVO<ProductVO> pageVo = PageVO.<ProductVO>builder()
                .totalPage(total)
                .pageNo(arg.getPageNo())
                .pageSize(arg.getPageSize())
                .rows(productVOList)
                .build();
        return Result.newSuccess(pageVo);
    }

    /**通过商品id查询商品*/
    public Result<ProductVO> findProductById(String productId){
        if (productId == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        ProductInfo productInfo = productInfoRepository.findProductInfoByProductId(productId);
        if (productInfo == null){
            return Result.newResult(ResultEnum.NO_GOODS_MSG);
        }
        ProductVO productVO = new ProductVO();
        BeanUtils.copyProperties(productInfo,productVO);
        ProductCategory productCategory = productCategoryRepository.findProductCategoryByCategoryType(productInfo.getCategoryType());
        productVO.setProductCategory(productCategory);
        return Result.newSuccess(productVO);
    }

    /**用户发布二手商品信息，需要社区认证，具体什么认证方式之后再讨论一下*/
    public Result<Map<String,String>> publishProduct(ProductForm productForm){
        //验证token
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)){
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }

        String productId = idWorker.nextId() + "";

        ProductInfo productInfo = new ProductInfo();
        BeanUtils.copyProperties(productForm,productInfo);
        productInfo.setProductId(productId);
        productInfo.setProductStatus(ProductStatusEnums.UP.getCode());
        productInfo.setMarketId(0); //0表示是用户发布的商品
        if (productForm.getProductStock() == null){
            productInfo.setProductStock(1);
        }
        productInfoRepository.save(productInfo);

        Map<String,String> map = new HashMap<>();
        map.put("productId",productId);
        return Result.newSuccess(map);
    }

    /**根据类目编号查询商品*/
    public Result<PageVO<ProductVO>> findProductByCategory(Integer categoryType,PageArg arg){
        arg.validate();
        if (categoryType == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Pageable pageable = PageRequest.of(arg.getPageNo() - 1,arg.getPageSize());
        Page<ProductInfo> productInfoPage = productInfoRepository.findProductInfoByCategoryTypeOrderByUpdateTimeDesc(categoryType,pageable);

        List<ProductInfo> productInfoList = productInfoPage.getContent();
        List<ProductVO> productVOList = new ArrayList<>();
        for (ProductInfo productInfo : productInfoList){
            ProductVO productVO = new ProductVO();
            BeanUtils.copyProperties(productInfo,productVO);
            ProductCategory productCategory = productCategoryRepository.findProductCategoryByCategoryType(productInfo.getCategoryType());
            productVO.setProductCategory(productCategory);
            productVOList.add(productVO);
        }
        int total = productInfoPage.getTotalPages();
        //构建pageVo对象
        PageVO<ProductVO> pageVo = PageVO.<ProductVO>builder()
                .totalPage(total)
                .pageNo(arg.getPageNo())
                .pageSize(arg.getPageSize())
                .rows(productVOList)
                .build();
        return Result.newSuccess(pageVo);
    }

    /**添加商品类目信息*/
    public Result<Void> addCategory(CategoryForm categoryForm){
        ProductCategory productCategory = new ProductCategory();
        BeanUtils.copyProperties(categoryForm,productCategory);
        productCategoryRepository.save(productCategory);
        return Result.newSuccess();
    }

    /**更新商品类目信息*/
    public Result<Void> updateCategory(Integer categoryId ,CategoryForm categoryForm){
        if (productCategoryRepository.findProductCategoryByCategoryId(categoryId) == null){
            return Result.newResult(ResultEnum.CATEGORY_NOT_EXIST);
        }
        ProductCategory productCategory = new ProductCategory();
        BeanUtils.copyProperties(categoryForm,productCategory);
        productCategory.setCategoryId(categoryId);
        productCategoryRepository.save(productCategory);
        return Result.newSuccess();
    }

    /**删除商品类目信息*/
    public Result<Void> deleteCategory(Integer categoryId){
       if (productCategoryRepository.findProductCategoryByCategoryId(categoryId) == null){
           return Result.newResult(ResultEnum.CATEGORY_NOT_EXIST);
       }
       productCategoryRepository.deleteById(categoryId);
        return Result.newSuccess();
    }

    /**列出所有类目*/
    public Result<List<ProductCategory>> listCategory(){
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        if (productCategoryList.isEmpty()){
            return Result.newResult(ResultEnum.CATEGORY_NOT_EXIST);
        }
        return Result.newSuccess(productCategoryList);
    }
}
