package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类，用于存储和管理订单的详细信息。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders implements Serializable {

    /**
     * 订单状态常量定义。
     */
    public static final Integer PENDING_PAYMENT = 1; // 待付款
    public static final Integer TO_BE_CONFIRMED = 2;   // 待接单
    public static final Integer CONFIRMED = 3;        // 已接单
    public static final Integer DELIVERY_IN_PROGRESS = 4; // 派送中
    public static final Integer COMPLETED = 5;       // 已完成
    public static final Integer CANCELLED = 6;        // 已取消

    /**
     * 支付状态常量定义。
     */
    public static final Integer UN_PAID = 0;         // 未支付
    public static final Integer PAID = 1;            // 已支付
    public static final Integer REFUND = 2;          // 退款

    private static final long serialVersionUID = 1L;

    /**
     * 订单的唯一标识符ID。
     */
    private Long id;

    /**
     * 订单号。
     */
    private String number;

    /**
     * 订单状态，对应前面的订单状态常量。
     */
    private Integer status;

    /**
     * 下单用户的ID。
     */
    private Long userId;

    /**
     * 地址簿ID，关联到用户地址信息。
     */
    private Long addressBookId;

    /**
     * 下单时间。
     */
    private LocalDateTime orderTime;

    /**
     * 结账时间。
     */
    private LocalDateTime checkoutTime;

    /**
     * 支付方式，例如：1代表微信，2代表支付宝。
     */
    private Integer payMethod;

    /**
     * 支付状态，对应前面的支付状态常量。
     */
    private Integer payStatus;

    /**
     * 实收金额。
     */
    private BigDecimal amount;

    /**
     * 订单备注信息。
     */
    private String remark;

    /**
     * 用户名，下单用户的姓名。
     */
    private String userName;

    /**
     * 下单用户的手机号。
     */
    private String phone;

    /**
     * 用户的地址信息。
     */
    private String address;

    /**
     * 收货人姓名。
     */
    private String consignee;

    /**
     * 如果订单被取消，记录取消原因。
     */
    private String cancelReason;

    /**
     * 如果订单被拒绝，记录拒绝原因。
     */
    private String rejectionReason;

    /**
     * 订单取消时间。
     */
    private LocalDateTime cancelTime;

    /**
     * 预计送达时间。
     */
    private LocalDateTime estimatedDeliveryTime;

    /**
     * 配送状态，例如：1代表立即送出，0代表选择具体时间。
     */
    private Integer deliveryStatus;

    /**
     * 订单的实际送达时间。
     */
    private LocalDateTime deliveryTime;

    /**
     * 打包费用。
     */
    private int packAmount;

    /**
     * 餐具数量。
     */
    private int tablewareNumber;

    /**
     * 餐具数量状态，例如：1代表按餐量提供，0代表选择具体数量。
     */
    private Integer tablewareStatus;

}