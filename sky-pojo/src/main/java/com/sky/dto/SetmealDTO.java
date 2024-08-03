package com.sky.dto;

import com.sky.entity.SetmealDish;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 套餐数据传输对象，用于封装套餐及其包含的菜品信息的传输数据。
 * 该对象通常用于API请求和响应中，以便于前端获取和操作套餐数据。
 */
@Data
public class SetmealDTO implements Serializable {

    /**
     * 套餐的唯一标识符ID。
     */
    private Long id;

    /**
     * 分类ID，套餐所属的分类。
     */
    private Long categoryId;

    /**
     * 套餐的名称，如“双人套餐”。
     */
    private String name;

    /**
     * 套餐的价格。
     */
    private BigDecimal price;

    /**
     * 套餐的状态，0表示停用，1表示启用。
     */
    private Integer status;

    /**
     * 套餐的描述信息，可能包含套餐的特色、包含菜品等。
     */
    private String description;

    /**
     * 套餐的图片链接，可以是相对路径或绝对URL。
     */
    private String image;

    /**
     * 套餐中包含的菜品列表，每个菜品可能包含名称、价格等信息。
     */
    private List<SetmealDish> setmealDishes = new ArrayList<>();

}