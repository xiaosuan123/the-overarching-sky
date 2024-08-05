/**
 * 套餐服务接口，提供套餐相关的业务操作。
 */
package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;

/**
 * 服务接口，用于处理与套餐相关的业务逻辑。
 */
public interface SetmealService {

    /**
     * 保存套餐信息并关联菜品。
     *
     * @param setmealDTO 套餐数据传输对象。
     */
    void saveWithDish(SetmealDTO setmealDTO);

    /**
     * 分页查询套餐信息。
     *
     * @param setmealPageQueryDTO 分页查询数据传输对象。
     * @return 分页结果，包含套餐列表和分页信息。
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 批量删除套餐。
     *
     * @param ids 要删除的套餐ID列表。
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据ID获取套餐详情，包括关联的菜品信息。
     *
     * @param id 套餐ID。
     * @return 套餐视图对象，包含套餐和菜品的详细信息。
     */
    SetmealVO getByIdWithDish(Long id);

    /**
     * 更新套餐信息。
     *
     * @param setmealDTO 包含更新信息的套餐数据传输对象。
     */
    void update(SetmealDTO setmealDTO);

    /**
     * 开启或停止套餐服务。
     *
     * @param status 状态值，通常1表示开启，0表示停止。
     * @param id     套餐ID。
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据条件查询套餐列表。
     *
     * @param setmeal 套餐实体对象，包含查询条件。
     * @return 套餐列表。
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐ID获取关联的菜品项列表。
     *
     * @param id 套餐ID。
     * @return 菜品项视图对象列表。
     */
    List<DishItemVO> getDishItemById(Long id);
}