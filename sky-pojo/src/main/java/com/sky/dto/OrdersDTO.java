package com.sky.dto;

import com.sky.entity.OrderDetail;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单数据传输对象，用于封装订单及其明细的相关信息。
 * 该对象通常用于API请求和响应中，以便于前端获取和操作订单数据。
 */
@Data
public class OrdersDTO implements Serializable {

    /**
     * 订单的唯一标识符ID。
     */
    private Long id;

    /**
     * 订单号，用于唯一标识一个订单。
     */
    private String number;

    /**
     * 订单状态，不同的状态代表订单不同的处理阶段：
     * 1 - 待付款
     * 2 - 待派送
     * 3 - 已派送
     * 4 - 已完成
     * 5 - 已取消
     */
    private Integer status;

    /**
     * 下单用户的ID，关联到用户实体。
     */
    private Long userId;

    /**
     * 地址簿ID，关联到用户地址信息。
     */
    private Long addressBookId;

    /**
     * 下单时间，记录用户下单的确切时间。
     */
    private LocalDateTime orderTime;

    /**
     * 结账时间，记录订单完成结账的时间。
     */
    private LocalDateTime checkoutTime;

    /**
     * 支付方式，可能的值包括：
     * 1 - 微信支付
     * 2 - 支付宝支付
     */
    private Integer payMethod;

    /**
     * 实收金额，表示用户实际支付的金额。
     */
    private BigDecimal amount;

    /**
     * 订单备注，用户或商家可以添加的额外说明。
     */
    private String remark;

    /**
     * 下单用户的姓名。
     */
    private String userName;

    /**
     * 下单用户的手机号。
     */
    private String phone;

    /**
     * 用户的收货地址。
     */
    private String address;

    /**
     * 收货人的姓名。
     */
    private String consignee;

    /**
     * 订单明细列表，包含订单中所有菜品或套餐的详细信息。
     */
    private List<OrderDetail> orderDetails;

}