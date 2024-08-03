package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 套餐实体类，用于存储和管理套餐的相关信息。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Setmeal implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 套餐的唯一标识符ID。
     */
    private Long id;

    /**
     * 分类ID，套餐所属的分类。
     */
    private Long categoryId;

    /**
     * 套餐的名称。
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
     * 套餐的描述信息，可以包含套餐包含的菜品、特色等。
     */
    private String description;

    /**
     * 套餐的图片链接。
     */
    private String image;

    /**
     * 套餐创建的时间。
     */
    private LocalDateTime createTime;

    /**
     * 套餐最后更新的时间。
     */
    private LocalDateTime updateTime;

    /**
     * 创建此套餐记录的用户ID。
     */
    private Long createUser;

    /**
     * 最后更新此套餐记录的用户ID。
     */
    private Long updateUser;
}