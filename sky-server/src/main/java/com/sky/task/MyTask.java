package com.sky.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * MyTask 类是一个定时任务组件
 * 它通过 @Component 注解声明为一个 Spring Bean
 * 使用 @Slf4j 注解进行日志记录
 */
@Component
@Slf4j
public class MyTask {

    /**
     * 执行定时任务
     * 该方法记录当前时间，用于标识任务的执行时刻
     */
    public void executeTask() {
        log.info("定时任务开始执行：{}", new Date());
    }
}
