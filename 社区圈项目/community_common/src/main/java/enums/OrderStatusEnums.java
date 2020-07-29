package enums;

import lombok.Getter;

//订单状态的枚举
@Getter
public enum OrderStatusEnums {
    NEW(0,"新订单"),
    FINISHED(1,"订单已结账"),
    CANCEL(2,"取消订单"),
    ;
    private Integer code;
    private String msg;

    OrderStatusEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
