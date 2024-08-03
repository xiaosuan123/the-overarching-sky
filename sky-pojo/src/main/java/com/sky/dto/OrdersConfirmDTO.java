package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 订单确认数据传输对象，用于封装订单状态更新所需的数据。
 * 该对象通常用于API请求中，当需要确认订单状态时使用。
 */
@Data
public class OrdersConfirmDTO implements Serializable {

    /**
     * 订单的唯一标识符ID，用于指定需要确认状态的订单。
     */
    private Long id;

    /**
     * 订单状态，表示订单当前的业务状态。
     * 可能的值包括：
     * 1 - 待付款
     * 2 - 待接单
     * 3 - 已接单
     * 4 - 派送中
     * 5 - 已完成
     * 6 - 已取消
     * 7 - 退款
     */
    private Integer status;

}