package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * 批量插入口味数据
     * @param flavors
     */
    @AutoFill(value = OperationType.INSERT)
    void insertBatch(List<DishFlavor> flavors);
    /**
     * 删除口味数据
     * @param dishId
     */
    @Delete("delete from dish_flavor where dish_id=#{dishId}")
    void deleteByDishId(Long dishId);
    /**
     * 查询口味数据
     * @param dishId
     * @return
     */
    @Select("select *from dish_flavor where dish_id=#{dishId}")
    List<DishFlavor> getByDishId(Long dishId);
}
