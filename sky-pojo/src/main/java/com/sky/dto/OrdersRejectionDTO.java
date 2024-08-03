package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 订单拒绝数据传输对象，用于封装订单拒绝操作所需的数据。
 * 该对象通常用于API请求中，传递订单拒绝的具体原因。
 */
@Data
public class OrdersRejectionDTO implements Serializable {

    /**
     * 订单的唯一标识符ID，用于指定需要拒绝的订单。
     */
    private Long id;

    /**
     * 订单拒绝原因，用于记录拒绝订单的具体理由，提供给用户或管理员参考。
     */
    private String rejectionReason;

}