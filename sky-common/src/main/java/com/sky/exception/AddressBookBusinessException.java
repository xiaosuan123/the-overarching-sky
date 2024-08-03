package com.sky.exception;

/**
 * 地址簿业务异常类，用于处理与地址簿相关的业务逻辑异常。
 * 此异常继承自{@link BaseException}，提供了业务特定的异常处理功能。
 */
public class AddressBookBusinessException extends BaseException {

    /**
     * 构造一个新的地址簿业务异常，使用指定的错误消息。
     *
     * @param msg 异常消息的详细描述，提供关于业务异常的更多信息。
     */
    public AddressBookBusinessException(String msg) {
        super(msg);
    }
}