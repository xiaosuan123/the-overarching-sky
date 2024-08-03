package com.sky.constant;

/**
 * 状态常量类，定义了系统中常用的启用和禁用状态标识。
 * 这些常量通常用于控制功能模块、用户账户、数据记录等的启用状态。
 */
public class StatusConstant {

    /**
     * 启用状态标识。
     * 在系统中，此值通常用来表示账户、功能或记录已被激活并可正常使用。
     */
    public static final Integer ENABLE = 1;

    /**
     * 禁用状态标识。
     * 在系统中，此值通常用来表示账户、功能或记录已被禁用，不可使用或不可见。
     */
    public static final Integer DISABLE = 0;

}