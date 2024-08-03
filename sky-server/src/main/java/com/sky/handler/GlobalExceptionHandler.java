package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，用于集中处理项目中抛出的业务异常。
 * 通过{@link RestControllerAdvice}注解标识为Spring MVC的全局异常处理类，
 * 可以捕获并处理所有Controller中抛出的异常。
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获并处理业务异常。
     * 当项目中抛出{@link BaseException}类型的异常时，将调用此方法进行处理。
     *
     * @param ex 捕获到的业务异常。
     * @return 统一的响应结果，包含异常信息。
     */
    @ExceptionHandler(BaseException.class)
    public Result exceptionHandler(BaseException ex) {
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 异常处理器，用于处理数据库完整性约束冲突异常。
     *
     * @param ex 捕获到的数据库完整性约束冲突异常。
     * @return 处理结果，根据不同的异常信息返回相应的错误消息。
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        String message = ex.getMessage();
        // 检查异常消息是否包含"Duplicate entry"关键字
        if (message.contains("Duplicate entry")) {
            String[] split = message.split(" "); // 这里应该是按空格分割，而不是按空字符分割
            String username = split[2]; // 假设分割后的数组中用户名是第三个元素
            String msg = username + MessageConstant.ALREADY_EXISTS; // 拼接具体的错误信息
            return Result.error(msg); // 返回具体的错误信息
        } else {
            return Result.error(MessageConstant.UNKNOWN_ERROR); // 返回未知错误的通用信息
        }
    }
}