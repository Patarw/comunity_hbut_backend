package com.hbut.product.VO;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderMasterVO implements Serializable {

    /**订单id*/
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

    /**订单详情**/
    private List<OrderDetailVO> orderDetailVOList;
}
