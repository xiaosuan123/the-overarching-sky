package com.sky.exception;

/**
 * 密码错误异常类，用于表示用户输入的密码不正确的情况。
 * 此异常继承自{@link BaseException}，用于在用户认证过程中提供密码错误的反馈。
 */
public class PasswordErrorException extends BaseException {

    /**
     * 构造一个新的密码错误异常，使用默认的错误消息。
     */
    public PasswordErrorException() {
    }

    /**
     * 构造一个新的密码错误异常，使用指定的错误消息。
     *
     * @param msg 提供关于密码错误异常的自定义消息。
     */
    public PasswordErrorException(String msg) {
        super(msg);
    }
}