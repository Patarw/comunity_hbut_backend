package com.hbut.product.dao;


import com.hbut.product.entity.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**商品信息dao**/
public interface ProductInfoRepository extends JpaRepository<ProductInfo,String> {
    ProductInfo findProductInfoByProductId(String productId);
    Page<ProductInfo> findProductInfoByProductStatusOrderByUpdateTimeDesc(Integer productStatus, Pageable pageable);
    Page<ProductInfo> findProductInfoByCategoryTypeOrderByUpdateTimeDesc(Integer categoryType,Pageable pageable);
}
