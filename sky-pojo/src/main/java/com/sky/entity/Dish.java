package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 菜品实体类，用于存储和管理菜品的相关信息。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dish implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜品的唯一标识符ID。
     */
    private Long id;

    /**
     * 菜品名称。
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
     * 菜品的图片链接。
     */
    private String image;

    /**
     * 菜品的描述信息，可以包含菜品的制作材料、制作方法等。
     */
    private String description;

    /**
     * 菜品状态，0表示停售，1表示起售。
     */
    private Integer status;

    /**
     * 菜品创建的时间。
     */
    private LocalDateTime createTime;

    /**
     * 菜品最后更新的时间。
     */
    private LocalDateTime updateTime;

    /**
     * 创建此菜品的用户ID。
     */
    private Long createUser;

    /**
     * 最后更新此菜品的用户ID。
     */
    private Long updateUser;
}