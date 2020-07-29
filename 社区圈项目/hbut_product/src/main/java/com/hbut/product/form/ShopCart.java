package com.hbut.product.form;

import lombok.Data;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;


@Data
public class ShopCart implements Serializable {

    /**商品id*/
    private String productId;

    /**购买的数量*/
    private Integer quantity ;
}
