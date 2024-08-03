package com.sky.dto;

import com.sky.entity.DishFlavor;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜品数据传输对象，用于封装菜品相关信息的传输数据。
 * 该对象通常用于API请求和响应中，以便于前端获取和操作菜品数据。
 */
@Data
public class DishDTO implements Serializable {

    /**
     * 菜品的唯一标识符ID。
     */
    private Long id;

    /**
     * 菜品的名称，如“宫保鸡丁”。
     */
    private String name;

    /**
     * 菜品所属分类的ID。
     */
    private Long categoryId;

    /**
     * 菜品的价格。
     */
    private BigDecimal price;

    /**
     * 菜品的图片链接，可以是相对路径或绝对URL。
     */
    private String image;

    /**
     * 菜品的描述信息，可能包含制作方法、食材介绍等。
     */
    private String description;

    /**
     * 菜品的上下架状态，0表示停售，1表示起售。
     */
    private Integer status;

    /**
     * 菜品可提供的口味列表，每个口味可能包含不同的配料或调料。
     */
    private List<DishFlavor> flavors = new ArrayList<>();

}