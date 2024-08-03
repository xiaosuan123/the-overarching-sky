package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单明细实体类，用于存储和管理订单中各个菜品或套餐的详细信息。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单明细的唯一标识符ID。
     */
    private Long id;

    /**
     * 菜品或套餐的名称。
     */
    private String name;

    /**
     * 关联的订单ID。
     */
    private Long orderId;

    /**
     * 如果明细是菜品，这是菜品的ID；如果是套餐，此字段应为null。
     */
    private Long dishId;

    /**
     * 如果明细是套餐，这是套餐的ID；如果是菜品，此字段应为null。
     */
    private Long setmealId;

    /**
     * 菜品的口味，例如“麻辣”、“酸甜”等。
     */
    private String dishFlavor;

    /**
     * 菜品或套餐的数量。
     */
    private Integer number;

    /**
     * 菜品或套餐的总金额，由单价和数量计算得出。
     */
    private BigDecimal amount;

    /**
     * 菜品或套餐的图片链接。
     */
    private String image;

}