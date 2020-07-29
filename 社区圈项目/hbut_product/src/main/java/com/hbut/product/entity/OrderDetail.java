package com.hbut.product.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**订单详情**/
@Data
@Entity
@Table(name = "order_detail")
public class OrderDetail implements Serializable {

    @Id
    private String detailId;

    /**订单id*/
    private String orderId;

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
