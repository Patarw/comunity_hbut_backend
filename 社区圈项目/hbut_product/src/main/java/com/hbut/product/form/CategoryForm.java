package com.hbut.product.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class CategoryForm implements Serializable {

    /**类目名字**/
    @NotEmpty(message = "类目名字必填")
    private String categoryName;

    /**类目编号**/
    @NotNull(message = "类目编号必填")
    private Integer categoryType;

}
