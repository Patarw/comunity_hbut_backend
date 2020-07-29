package enums;

import lombok.Getter;
import lombok.ToString;

/**
 * 统一状态码响应
 */
@ToString
public enum ResultEnum {
    SYSTEM_ERROR(-1, "系统错误,请联系管理员"),
    SUCCESS(1, "成功"),
    LOGIN_ERROR(2,"用户名或密码错误"),
    PARAM_ERROR(3, "输入参数错误,请校验参数重新输入"),
    SQL_UNIQUE_ERROR(4, "数据库已有重复值"),
    REMOTE_ERROR(5, "远程调用失败"),
    AUTHENTICATION_ERROR(6, "您还未登录"),
    USER_NOT_EXIST(7, "用户不存在"),
    NO_GOODS_MSG(8,"商品不存在"),
    ROLE_ERROR(9, "您不具备该角色权限"),
    NO_LABEL_MSG(10,"标签不存在"),
    NO_ARTICLE_MSG(13,"文章不存在"),
    REGISTER_ERROR(14,"注册失败，验证码错误"),
    REGISTER_MOBILE_ERROR(15,"注册失败，电话号码已经被注册"),
    FRIEND_ALREADY_EXIST(16,"好友已添加，无需重复添加"),
    PRODUCT_STOCK_ERROR(17,"商品库存不足"),
    ORDER_NOT_EXIST(18,"订单不存在"),
    ORDER_STATUS_ERROR(19,"订单状态不正确，订单已支付或者已取消"),
    CART_EMPTY(20,"购物车不能为空"),
    CATEGORY_NOT_EXIST(21,"类目不存在"),
    ;

    @Getter
    private Integer code;
    @Getter
    private String msg;

    ResultEnum(Integer code, String errMsg) {
        this.code = code;
        this.msg = errMsg;
    }
}
