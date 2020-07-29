package VO;

import enums.ResultEnum;
import lombok.Data;

@Data
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;

    public static <T> Result<T> newResult(ResultEnum resultEnum) {
        Result<T> result = new Result<T>();
        result.setCode(resultEnum.getCode());
        result.setMsg(resultEnum.getMsg());
        return result;
    }

    public static <T> Result<T> newResult(ResultEnum resultEnum, String errorMsg) {
        Result<T> result = newResult(resultEnum);
        result.setMsg(errorMsg);
        return result;
    }

    public static <T> Result<T> newSuccess(T data) {
        Result<T> result = newResult(ResultEnum.SUCCESS);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> newSuccess() {
        Result<T> result = newResult(ResultEnum.SUCCESS);
        return result;
    }

    public static <T> Result<T> newError(String msg) {
        Result<T> result = newResult(ResultEnum.SYSTEM_ERROR);
        result.setMsg(msg);
        return result;
    }
}
