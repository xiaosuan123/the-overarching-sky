package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 员工分页查询数据传输对象，用于封装员工分页查询的请求参数。
 * 该对象用于API请求中，帮助系统根据员工姓名进行筛选，并分页返回员工数据。
 */
@Data
public class EmployeePageQueryDTO implements Serializable {

    /**
     * 员工的姓名，可用于模糊搜索员工。
     */
    private String name;

    /**
     * 请求的当前页码，用于分页显示员工列表，通常从1开始计数。
     */
    private int page;

    /**
     * 每页显示的员工记录数，用于分页显示员工列表。
     */
    private int pageSize;

}