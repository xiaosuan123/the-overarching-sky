package com.sky.exception;

/**
 * 订单业务异常类，用于处理与订单相关的业务逻辑异常情况。
 * 此异常继承自{@link BaseException}，提供了订单业务特定的异常处理机制。
 */
public class OrderBusinessException extends BaseException {

    /**
     * 构造一个新的订单业务异常，使用指定的详细错误消息。
     *
     * @param msg 异常消息的详细描述，提供关于订单业务异常的更多信息。
     */
    public OrderBusinessException(String msg) {
        super(msg);
    }
}