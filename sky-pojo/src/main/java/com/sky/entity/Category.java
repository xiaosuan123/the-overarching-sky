package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 分类实体类，用于存储和管理分类信息。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类的ID，唯一标识一个分类。
     */
    private Long id;

    /**
     * 类型：1表示菜品分类，2表示套餐分类。
     */
    private Integer type;

    /**
     * 分类的名称。
     */
    private String name;

    /**
     * 分类的排序值，数值越小，分类越靠前。
     */
    private Integer sort;

    /**
     * 分类状态，0表示禁用，1表示启用。
     */
    private Integer status;

    /**
     * 分类创建的时间。
     */
    private LocalDateTime createTime;

    /**
     * 分类最后更新的时间。
     */
    private LocalDateTime updateTime;

    /**
     * 创建此分类的用户ID。
     */
    private Long createUser;

    /**
     * 最后更新此分类的用户ID。
     */
    private Long updateUser;
}