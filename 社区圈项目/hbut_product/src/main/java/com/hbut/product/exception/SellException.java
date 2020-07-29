package com.hbut.product.exception;


import enums.ResultEnum;

/**异常**/
public class SellException extends RuntimeException {
    private Integer code;

    public SellException(ResultEnum resultEnums) {
        super(resultEnums.getMsg());
        this.code = resultEnums.getCode();
    }

    public SellException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
