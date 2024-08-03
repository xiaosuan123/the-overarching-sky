package com.sky.exception;

/**
 * 套餐启用失败异常类，当尝试启用套餐但操作未能成功完成时抛出。
 * 此异常继承自{@link BaseException}，用于表示套餐启用过程中出现的问题。
 */
public class SetmealEnableFailedException extends BaseException {

    /**
     * 构造一个新的套餐启用失败异常，使用默认的错误消息。
     */
    public SetmealEnableFailedException() {
    }

    /**
     * 构造一个新的套餐启用失败异常，使用指定的错误消息。
     *
     * @param msg 异常消息的详细描述，提供关于套餐启用失败原因的更多信息。
     */
    public SetmealEnableFailedException(String msg) {
        super(msg);
    }
}