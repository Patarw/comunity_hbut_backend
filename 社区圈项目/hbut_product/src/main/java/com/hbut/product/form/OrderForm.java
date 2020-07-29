package com.hbut.product.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**用户下单表单**/
@Data
public class OrderForm implements Serializable{
    /**买家姓名*/
    @NotEmpty(message = "姓名必填")
    private String buyerName;

    /**买家电话*/
    @NotEmpty(message = "电话必填")
    private String buyerPhone;

    /**买家地址*/
    @NotEmpty(message = "地址必填")
    private String buyerAddress;

    /**买家id*/
    @NotNull(message = "用户id必填")
    private Integer buyerId;

    /**订单总价*/
    @NotNull(message = "订单总价不能为空")
    private BigDecimal orderAmount;

    /**购物车*/
    @NotEmpty(message = "购物车不能为空")
    private List<ShopCart> shopCartList;
}
