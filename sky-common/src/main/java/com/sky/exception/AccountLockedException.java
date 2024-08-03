package com.sky.exception;

/**
 * 账号被锁定异常类，当用户账号因为多次尝试登录失败等原因被锁定时抛出。
 * 继承自{@link BaseException}，提供了基本的异常处理功能。
 */
public class AccountLockedException extends BaseException {

    /**
     * 构造一个新的账号被锁定异常，使用默认错误消息。
     */
    public AccountLockedException() {
    }

    /**
     * 构造一个新的账号被锁定异常，使用提供的错误消息。
     *
     * @param msg 异常消息的详细描述。
     */
    public AccountLockedException(String msg) {
        super(msg);
    }

}