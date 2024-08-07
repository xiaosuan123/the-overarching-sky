package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

import java.time.LocalDateTime;

/**
 * WorkspaceService 接口定义了一套餐饮管理后台的核心服务功能，
 * 包括获取业务数据、订单概览、菜品概览以及套餐概览等。
 */
public interface WorkspaceService {

    /**
     * 获取指定时间范围内的业务数据。
     *
     * @param begin 开始时间
     * @param end 结束时间
     * @return 包含指定时间范围内业务数据的 BusinessDataVO 对象
     */
    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);

    /**
     * 获取订单概览数据。
     *
     * @return 包含订单概览数据的 OrderOverViewVO 对象
     */
    OrderOverViewVO getOrderOverView();

    /**
     * 获取菜品概览数据。
     *
     * @return 包含菜品概览数据的 DishOverViewVO 对象
     */
    DishOverViewVO getDishOverView();

    /**
     * 获取套餐概览数据。
     *
     * @return 包含套餐概览数据的 SetmealOverViewVO 对象
     */
    SetmealOverViewVO getSetmealOverView();
}
