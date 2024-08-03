package com.sky.exception;

/**
 * 密码修改失败异常类，当用户尝试修改密码但操作未能成功完成时抛出。
 * 此异常继承自{@link BaseException}，用于表示密码修改过程中出现的问题。
 */
public class PasswordEditFailedException extends BaseException {

    /**
     * 使用指定的错误消息构造一个新的密码修改失败异常。
     *
     * @param msg 异常消息的详细描述，提供关于密码修改失败原因的更多信息。
     */
    public PasswordEditFailedException(String msg) {
        super(msg);
    }
}