package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 菜品分页查询数据传输对象，用于封装前端发送的菜品分页查询请求参数。
 * 该对象用于API请求中，帮助后端理解查询条件并返回相应的菜品数据。
 */
@Data
public class DishPageQueryDTO implements Serializable {

    /**
     * 请求的当前页码，通常从1开始计数，用于分页显示菜品列表。
     */
    private int page;

    /**
     * 每页显示的菜品数量，用于分页显示菜品列表。
     */
    private int pageSize;

    /**
     * 菜品名称的搜索关键字，可用于模糊匹配菜品名称。
     */
    private String name;

    /**
     * 分类ID，用于筛选属于特定分类的菜品。
     */
    private Integer categoryId;

    /**
     * 菜品的状态，0表示禁用（即停售），1表示启用（即起售）。
     */
    private Integer status;

}