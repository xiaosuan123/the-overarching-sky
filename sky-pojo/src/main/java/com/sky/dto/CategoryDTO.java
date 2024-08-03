package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 类别数据传输对象，用于封装类别信息的传输数据。
 * 该对象通常用于API请求和响应中，以便于前端获取和操作类别数据。
 */
@Data
public class CategoryDTO implements Serializable {

    /**
     * 类别的唯一标识符ID。
     */
    private Long id;

    /**
     * 类型，1表示菜品分类，2表示套餐分类。
     */
    private Integer type;

    /**
     * 分类的名称，例如“川菜”、“粤菜”等。
     */
    private String name;

    /**
     * 分类的排序值，用于在显示时确定类别的顺序。
     */
    private Integer sort;

}