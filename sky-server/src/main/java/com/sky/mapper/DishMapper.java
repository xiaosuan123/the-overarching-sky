package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 菜品数据访问层接口，提供对菜品数据的操作。
 */
@Mapper
public interface DishMapper {

    /**
     * 根据菜品分类ID统计菜品数量。
     *
     * @param categoryId 分类ID，用于筛选特定分类下的菜品数量
     * @return 分类下菜品的数量
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

}