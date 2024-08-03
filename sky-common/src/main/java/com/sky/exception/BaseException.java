package com.sky.exception;

/**
 * 基础业务异常类，用于封装应用程序中的业务逻辑异常。
 * 此异常继承自{@link RuntimeException}，表示业务操作中出现的非预期情况。
 */
public class BaseException extends RuntimeException {

    /**
     * 构造一个新的业务异常，使用默认的错误消息。
     */
    public BaseException() {
    }

    /**
     * 构造一个新的业务异常，使用指定的错误消息。
     *
     * @param msg 异常消息的详细描述，提供关于业务异常的更多信息。
     */
    public BaseException(String msg) {
        super(msg);
    }

}