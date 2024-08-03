package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 套餐分页查询数据传输对象，用于封装套餐分页查询的请求参数。
 * 该对象用于API请求中，帮助系统根据指定的条件进行套餐数据的分页查询。
 */
@Data
public class SetmealPageQueryDTO implements Serializable {

    /**
     * 请求的当前页码，用于分页显示套餐列表，通常从1开始计数。
     */
    private int page;

    /**
     * 每页显示的套餐数量，用于分页显示套餐列表。
     */
    private int pageSize;

    /**
     * 套餐名称的搜索关键字，可用于模糊匹配套餐名称。
     */
    private String name;

    /**
     * 分类ID，用于筛选属于特定分类的套餐。
     */
    private Integer categoryId;

    /**
     * 套餐的状态，用于筛选套餐的启用或禁用状态。
     * 0表示禁用，1表示启用。
     */
    private Integer status;

}