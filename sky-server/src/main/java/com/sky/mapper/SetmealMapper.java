package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 套餐数据访问层接口，提供对套餐数据的操作。
 */
@Mapper
public interface SetmealMapper {

    /**
     * 根据分类ID统计套餐数量。
     *
     * @param id 分类ID
     * @return 分类下套餐的数量
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);
}