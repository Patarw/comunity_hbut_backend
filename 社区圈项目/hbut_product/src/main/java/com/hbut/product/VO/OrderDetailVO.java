package com.hbut.product.VO;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderDetailVO implements Serializable {

    private String detailId;

    /**商品id*/
    private String productId;

    /**商品名称*/
    private String productName;

    /**商品价格*/
    private BigDecimal productPrice;

    /**商品数量*/
    private Integer productQuantity;

    /**商品小图*/
    private String productIcon;

    /**创建时间*/
    private Date createTime;

    /**更新时间*/
    private Date updateTime;

}
