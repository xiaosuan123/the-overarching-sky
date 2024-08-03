package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * 菜品口味实体类，用于存储和管理菜品的不同口味选项。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishFlavor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜品口味的唯一标识符ID。
     */
    private Long id;

    /**
     * 关联的菜品ID。
     */
    private Long dishId;

    /**
     * 口味的名称，如“麻辣”、“酸甜”等。
     */
    private String name;

    /**
     * 口味的具体数据，通常是一个列表形式的字符串，表示该口味包含的具体食材或调料。
     */
    private String value;

}