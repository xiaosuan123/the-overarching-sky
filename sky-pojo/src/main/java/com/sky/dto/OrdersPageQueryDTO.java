package com.sky.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 订单分页查询数据传输对象，用于封装订单分页查询的请求参数。
 * 该对象用于API请求中，帮助系统根据指定的条件进行订单数据的分页查询。
 */
@Data
public class OrdersPageQueryDTO implements Serializable {

    /**
     * 请求的当前页码，用于分页显示订单列表，通常从1开始计数。
     */
    private int page;

    /**
     * 每页显示的订单数量，用于分页显示订单列表。
     */
    private int pageSize;

    /**
     * 订单号的搜索关键字，可用于模糊匹配订单号。
     */
    private String number;

    /**
     * 下单用户的手机号，可用于搜索特定用户的订单。
     */
    private String phone;

    /**
     * 订单状态，用于筛选特定状态的订单。
     * 状态值可参考订单状态定义，例如：1-待付款，2-待派送等。
     */
    private Integer status;

    /**
     * 查询的开始时间，用于指定订单时间范围查询的起始点。
     * 格式为 yyyy-MM-dd HH:mm:ss。
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginTime;

    /**
     * 查询的结束时间，用于指定订单时间范围查询的结束点。
     * 格式为 yyyy-MM-dd HH:mm:ss。
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 下单用户的ID，用于筛选特定用户的订单记录。
     */
    private Long userId;

}