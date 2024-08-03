package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 订单取消数据传输对象，用于封装订单取消操作所需的数据。
 * 该对象通常用于API请求中，传递订单取消的具体原因。
 */
@Data
public class OrdersCancelDTO implements Serializable {

    /**
     * 订单的唯一标识符ID，用于指定需要取消的订单。
     */
    private Long id;

    /**
     * 订单取消原因，用于记录取消订单的具体理由，提供给用户或管理员参考。
     */
    private String cancelReason;

}