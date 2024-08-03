package com.sky.exception;

/**
 * 登录失败异常类，当用户登录操作未能成功完成时抛出。
 * 此异常继承自{@link BaseException}，用于表示登录过程中出现的问题。
 */
public class LoginFailedException extends BaseException {
    /**
     * 使用指定的错误消息构造一个新的登录失败异常。
     *
     * @param msg 异常消息的详细描述，提供关于登录失败原因的更多信息。
     */
    public LoginFailedException(String msg) {
        super(msg);
    }
}