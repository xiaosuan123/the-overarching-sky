package com.sky.handler;

import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

}