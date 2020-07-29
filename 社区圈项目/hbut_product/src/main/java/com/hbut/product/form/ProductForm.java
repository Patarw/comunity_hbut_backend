package com.hbut.product.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**用户发布商品表单**/
@Data
public class ProductForm implements Serializable {

    /**名字*/
    @NotEmpty(message = "商品名字必填")
    private String productName;

    /**单价*/
    @NotNull(message = "商品价格必填")
    private BigDecimal productPrice;

    /**库存*/
    private Integer productStock;

    /**商品描述*/
    @NotEmpty(message = "商品描述必填")
    private String productDescription;

    /**商品图片*/
    private String productIcon;

    /**商品类目*/
    @NotNull(message = "商品类目必填")
    private Integer categoryType;

    /**卖家id*/
    @NotNull(message = "卖家id必填")
    private Integer userId;

}
