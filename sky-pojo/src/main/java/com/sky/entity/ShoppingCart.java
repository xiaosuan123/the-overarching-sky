package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车实体类，用于存储用户在购物过程中选择的菜品或套餐的相关信息。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 购物车项的唯一标识符ID。
     */
    private Long id;

    /**
     * 菜品或套餐的名称。
     */
    private String name;

    /**
     * 用户ID，表示购物车所属的用户。
     */
    private Long userId;

    /**
     * 如果购物车项是菜品，这是菜品的ID；如果是套餐，此字段应为null。
     */
    private Long dishId;

    /**
     * 如果购物车项是套餐，这是套餐的ID；如果是菜品，此字段应为null。
     */
    private Long setmealId;

    /**
     * 菜品的口味，仅当购物车项是菜品时有效。
     */
    private String dishFlavor;

    /**
     * 购物车中菜品或套餐的数量。
     */
    private Integer number;

    /**
     * 购物车中菜品或套餐的总金额，由单价和数量计算得出。
     */
    private BigDecimal amount;

    /**
     * 菜品或套餐的图片链接。
     */
    private String image;

    /**
     * 购物车项的创建时间。
     */
    private LocalDateTime createTime;

}