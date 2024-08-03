package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 分类分页查询数据传输对象，用于封装分页查询类别信息的请求数据。
 * 该对象通常用于接收前端发送的分页查询请求参数。
 */
@Data
public class CategoryPageQueryDTO implements Serializable {

    /**
     * 当前请求的页码，从1开始计数。
     */
    private int page;

    /**
     * 每页显示的记录数。
     */
    private int pageSize;

    /**
     * 分类的名称，可用于模糊查询分类名称。
     */
    private String name;

    /**
     * 分类的类型，1表示菜品分类，2表示套餐分类。可用于筛选特定类型的分类。
     */
    private Integer type;

}