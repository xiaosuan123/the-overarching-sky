package com.sky.exception;

/**
 * 禁止删除异常类，当尝试删除不允许删除的记录时抛出。
 * 此异常继承自{@link BaseException}，用于表示在业务逻辑中不允许执行删除操作的情况。
 */
public class DeletionNotAllowedException extends BaseException {

    /**
     * 构造一个新的禁止删除异常，使用指定的错误消息。
     *
     * @param msg 异常消息的详细描述，提供关于为何不允许删除的更多信息。
     */
    public DeletionNotAllowedException(String msg) {
        super(msg);
    }
}