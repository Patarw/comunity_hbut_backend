package com.hbut.product.dao;

import com.hbut.product.entity.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**商品类目dao**/
public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Integer> {
    ProductCategory findProductCategoryByCategoryId(Integer categoryId);
    ProductCategory findProductCategoryByCategoryType(Integer categoryType);
}
