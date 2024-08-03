package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 订单支付数据传输对象，用于封装订单支付操作所需的数据。
 * 该对象通常用于API请求中，传递订单支付的相关信息。
 */
@Data
public class OrdersPaymentDTO implements Serializable {

    /**
     * 订单号，用于唯一标识需要支付的订单。
     */
    private String orderNumber;

    /**
     * 付款方式，指定用户选择的支付方式。
     * 可能的值依赖于系统支持的支付方式，例如：
     * 1 - 微信支付
     * 2 - 支付宝支付
     * 等等，具体值需要根据实际业务逻辑确定。
     */
    private Integer payMethod;

}