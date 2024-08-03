package com.sky.exception;

/**
 * 购物车业务异常类，用于处理与购物车相关的业务逻辑异常情况。
 * 此异常继承自{@link BaseException}，提供了购物车业务特定的异常处理机制。
 */
public class ShoppingCartBusinessException extends BaseException {

    /**
     * 使用指定的错误消息构造一个新的购物车业务异常。
     *
     * @param msg 异常消息的详细描述，提供关于购物车业务异常的更多信息。
     */
    public ShoppingCartBusinessException(String msg) {
        super(msg);
    }
}