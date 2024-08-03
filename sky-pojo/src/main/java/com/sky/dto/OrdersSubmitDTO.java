package com.sky.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单提交数据传输对象，用于封装提交新订单时所需的数据。
 * 该对象通常用于API请求中，传递订单的详细信息。
 */
@Data
public class OrdersSubmitDTO implements Serializable {

    /**
     * 地址簿ID，用于指定订单的送货地址。
     */
    private Long addressBookId;

    /**
     * 付款方式，用户选择的支付方式。
     * 具体的支付方式数值应根据系统支持的支付方式来定义。
     */
    private int payMethod;

    /**
     * 订单备注，用户可以添加的额外说明或要求。
     */
    private String remark;

    /**
     * 预计送达时间，用户希望的订单送达时间。
     * 格式为 yyyy-MM-dd HH:mm:ss。
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime estimatedDeliveryTime;

    /**
     * 配送状态，用于指定配送的方式：
     * 1 - 立即送出
     * 0 - 选择具体时间
     */
    private Integer deliveryStatus;

    /**
     * 餐具数量，用户需要的餐具数量。
     */
    private Integer tablewareNumber;

    /**
     * 餐具数量状态，指定餐具数量的提供方式：
     * 1 - 按餐量提供
     * 0 - 选择具体数量
     */
    private Integer tablewareStatus;

    /**
     * 打包费用，订单的打包费用。
     */
    private Integer packAmount;

    /**
     * 总金额，订单的总支付金额。
     */
    private BigDecimal amount;

}