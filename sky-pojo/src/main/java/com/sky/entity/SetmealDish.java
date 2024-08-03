package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 套餐菜品关系实体类，用于表示套餐中包含的各个菜品及其相关信息。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetmealDish implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 套餐菜品关系的唯一标识符ID。
     */
    private Long id;

    /**
     * 套餐ID，表示该菜品属于哪个套餐。
     */
    private Long setmealId;

    /**
     * 菜品ID，表示套餐中包含的菜品。
     */
    private Long dishId;

    /**
     * 菜品名称，作为冗余字段存储于套餐菜品关系中，以便于快速获取菜品名称。
     */
    private String name;

    /**
     * 菜品的原价，用于在套餐中显示或计算套餐价值。
     */
    private BigDecimal price;

    /**
     * 菜品在套餐中的份数。
     */
    private Integer copies;

}