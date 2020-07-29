package com.hbut.product.entity;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**订单的用户信息**/
@Data
@Entity
@Table(name = "order_master")
public class OrderMaster implements Serializable {

    /**订单id*/
    @Id
    private String orderId;

    /**买家名字*/
    private String buyerName;

    /**买家电话*/
    private String buyerPhone;

    /**买家地址*/
    private String buyerAddress;

    /**买家的用户id*/
    private Integer buyerId;

    /**订单总金额*/
    private BigDecimal orderAmount;

    /**订单状态,默认为0 新下单*/
    private Integer orderStatus;

    /**支付状态,默认为0 未支付*/
    private Integer payStatus;

    /**创建时间*/
    private Date createTime;

    /**更新时间*/
    private Date updateTime;

}
