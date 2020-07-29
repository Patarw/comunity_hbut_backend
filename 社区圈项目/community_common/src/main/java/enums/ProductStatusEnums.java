package enums;


import lombok.Getter;

//枚举，商品状态//在Service层里面有用到
@Getter
public enum ProductStatusEnums {
    UP(0,"在架"),
    DOWN(1,"下架")
    ;
    private Integer code;
    private String msg;

    ProductStatusEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
