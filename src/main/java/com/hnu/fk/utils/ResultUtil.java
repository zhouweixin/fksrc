package com.hnu.fk.utils;

import com.hnu.fk.domain.Result;
import com.hnu.fk.exception.EnumExceptions;
import com.hnu.fk.exception.FkExceptions;

/**
 * 统一返回工具类
 *
 * @author zhouweixin
 */
public class ResultUtil {
    /**
     * 成功
     *
     * @param data
     * @return
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<T>();
        result.setCode(EnumExceptions.SUCCESS.getCode());
        result.setMessage(EnumExceptions.SUCCESS.getMessage());
        result.setData(data);
        return result;
    }

    /**
     * 成功
     *
     * @return
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 异常
     *
     * @param securityException
     * @return
     */
    public static <T> Result<T> error(FkExceptions securityException) {
        Result<T> result = new Result<T>();
        result.setCode(securityException.getCode());
        result.setMessage(securityException.getMessage());
        return result;
    }

    /**
     * 异常
     *
     * @param message
     * @return
     */
    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<T>();
        result.setCode(-2);
        result.setMessage(message);
        return result;
    }
}
