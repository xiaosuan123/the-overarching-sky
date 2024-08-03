package com.sky.exception;

/**
 * 账号不存在异常。
 */
public class AccountNotFoundException extends BaseException {

    /**
     * 构造一个新的账号不存在异常，使用默认的错误消息。
     */
    public AccountNotFoundException() {

    }

    /**
     * 构造一个新的账号不存在异常，使用指定的错误消息。
     *
     * @param msg 异常消息的详细描述。
     */
    public AccountNotFoundException(String msg) {
        super(msg);
    }

}