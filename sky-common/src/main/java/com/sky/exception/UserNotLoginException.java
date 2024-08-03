package com.sky.exception;

/**
 * 用户未登录异常类，当检测到用户未登录却尝试访问需要登录后才能操作的功能时抛出。
 * 此异常继承自{@link BaseException}，用于在用户身份验证过程中提供未登录的反馈。
 */
public class UserNotLoginException extends BaseException {

    /**
     * 构造一个新的用户未登录异常，使用默认的错误消息。
     */
    public UserNotLoginException() {
    }

    /**
     * 构造一个新的用户未登录异常，使用指定的错误消息。
     *
     * @param msg 提供关于用户未登录异常的自定义消息。
     */
    public UserNotLoginException(String msg) {
        super(msg);
    }
}