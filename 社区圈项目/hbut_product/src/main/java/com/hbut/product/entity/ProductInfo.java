package com.hbut.product.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**商品信息**/
@Data
@Entity
@Table(name = "product_info")
public class ProductInfo implements Serializable {
    @Id
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
}
