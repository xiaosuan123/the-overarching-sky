/**
 * 服务层接口，提供菜品（Dish）相关的业务逻辑操作。
 *
 * @author 你的姓名或开发团队名称
 * @version 1.0
 */
package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    /**
     * 保存菜品信息，包括其风味信息。
     *
     * @param dishDTO 包含菜品信息及风味信息的数据传输对象。
     */
    public void saveWithFlavor(DishDTO dishDTO);

    /**
     * 分页查询菜品信息。
     *
     * @param dishPageQueryDTO 包含分页查询条件的数据传输对象。
     * @return 分页结果，包含查询到的菜品列表及分页信息。
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 批量删除菜品信息。
     *
     * @param ids 要删除的菜品ID列表。
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据ID获取菜品及其风味信息的视图对象。
     *
     * @param id 菜品的唯一标识ID。
     * @return 包含菜品及其风味信息的视图对象。
     */
    DishVO getByIdWithFlavor(Long id);

    /**
     * 更新菜品信息，包括其风味信息。
     *
     * @param dishDTO 包含更新后的菜品信息及风味信息的数据传输对象。
     */
    void updateWithFlavor(DishDTO dishDTO);

    /**
     * 启动或停止菜品的可用状态。
     *
     * @param status 状态值，通常1表示启动，0表示停止。
     * @param id 菜品的唯一标识ID。
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据分类ID列出所有菜品。
     *
     * @param categoryId 分类ID。
     * @return 菜品列表。
     */
    List<Dish> list(Long categoryId);

    /**
     * 列出包含风味信息的菜品视图对象列表。
     *
     * @param dish 菜品实体对象，通常包含查询条件。
     * @return 包含风味信息的菜品视图对象列表。
     */
    List<DishVO> listWithFlavor(Dish dish);
}