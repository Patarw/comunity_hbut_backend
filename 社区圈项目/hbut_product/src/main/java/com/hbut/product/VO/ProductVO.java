package com.hbut.product.VO;

import com.hbut.product.entity.ProductCategory;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ProductVO implements Serializable {
    private String productId;

    /**名字*/
    private String productName;

    /**单价*/
    private BigDecimal productPrice;

    /**库存*/
    private Integer productStock;

    /**商品描述*/
    private String productDescription;

    /**商品图片*/
    private String productIcon;

    /**商品状态，0正常，1下架*/
    private Integer productStatus;

    /**卖家id*/
    private Integer marketId;

    /**类目编号*/
    private Integer categoryType;

    /**创建时间*/
    private Date createTime;

    /**更新时间*/
    private Date updateTime;

    /**类目信息**/
    private ProductCategory productCategory;
}
