package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 购物车数据传输对象，用于封装购物车中菜品或套餐的相关信息。
 * 该对象通常用于API请求和响应中，以便于前端添加或修改购物车中的商品。
 */
@Data
public class ShoppingCartDTO implements Serializable {

    /**
     * 菜品ID，如果购物车项是菜品，则此字段非空；如果是套餐，则为null。
     */
    private Long dishId;

    /**
     * 套餐ID，如果购物车项是套餐，则此字段非空；如果是菜品，则为null。
     */
    private Long setmealId;

    /**
     * 菜品的口味，当购物车项是菜品并且选择了特定口味时，此字段非空。
     * 例如，用户可以选择“麻辣”或“酸甜”等口味。
     */
    private String dishFlavor;

}