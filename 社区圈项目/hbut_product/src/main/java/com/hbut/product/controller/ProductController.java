package com.hbut.product.controller;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.hbut.product.VO.ProductVO;
import com.hbut.product.entity.ProductCategory;
import com.hbut.product.entity.ProductInfo;
import com.hbut.product.form.CategoryForm;
import com.hbut.product.form.ProductForm;
import com.hbut.product.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Api(tags = "商品管理接口")
@RestController
@RequestMapping("/product")
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductService productService;

    /**分页列出所有已上架商品*/
    @ApiOperation("分页列出所有已上架商品")
    @RequestMapping(value = "/listUpProduct",method = RequestMethod.POST)
    public Result<PageVO<ProductVO>> listUpProduct(@RequestBody PageArg arg){
        return productService.listUpProduct(arg);
    }

    /**通过商品id查询商品*/
    @ApiOperation("通过商品id查询商品信息")
    @RequestMapping(value = "/findProductById",method = RequestMethod.POST)
    public Result<ProductVO> findProductById(String productId){
        return productService.findProductById(productId);
    }

    /**用户发布二手商品信息，需要社区认证，具体什么认证方式之后再讨论一下*/
    @ApiImplicitParam(name = "Authorization", value = "需要token认证", required = false, dataType = "String",paramType = "header")
    @ApiOperation("用户发布二手商品信息，需要社区认证，具体什么认证方式之后再讨论一下")
    @RequestMapping(value = "/publishProduct",method = RequestMethod.POST)
    public Result<Map<String,String>> publishProduct(@Valid @RequestBody ProductForm productForm){
        return productService.publishProduct(productForm);
    }

    /**根据类目编号查询商品*/
    @ApiOperation("根据类目编号查询商品")
    @RequestMapping(value = "/findProductByCategory",method = RequestMethod.POST)
    public Result<PageVO<ProductVO>> findProductByCategory(Integer categoryType,@RequestBody PageArg arg){
        return productService.findProductByCategory(categoryType,arg);
    }

    /**添加商品类目信息*/
    @ApiOperation("添加商品类目信息")
    @RequestMapping(value = "/addCategory",method = RequestMethod.POST)
    public Result<Void> addCategory(@Valid @RequestBody CategoryForm categoryForm){
        return productService.addCategory(categoryForm);
    }

    /**更新商品类目信息*/
    @ApiOperation("更新商品类目信息")
    @RequestMapping(value = "/updateCategory",method = RequestMethod.POST)
    public Result<Void> updateCategory(Integer categoryId ,@Valid @RequestBody CategoryForm categoryForm){
        return productService.updateCategory(categoryId,categoryForm);
    }

    /**删除商品类目信息*/
    @ApiOperation("删除商品类目信息")
    @RequestMapping(value = "/deleteCategory",method = RequestMethod.POST)
    public Result<Void> deleteCategory(Integer categoryId){
        return productService.deleteCategory(categoryId);
    }

    /**列出所有类目*/
    @ApiOperation("列出所有类目")
    @RequestMapping(value = "/listCategory",method = RequestMethod.GET)
    public Result<List<ProductCategory>> listCategory(){
        return productService.listCategory();
    }
}
