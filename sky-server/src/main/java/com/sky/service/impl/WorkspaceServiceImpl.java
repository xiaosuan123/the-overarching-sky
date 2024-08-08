package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class WorkspaceServiceImpl implements WorkspaceService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 根据指定时间段获取业务数据
     * 此方法通过统计订单数量、营业额、有效订单完成率和新增用户数，
     * 为业务分析提供关键数据支持
     *
     * @param begin 开始时间（包含）
     * @param end   结束时间（包含）
     * @return 包含营业额、有效订单数量、订单完成率、商品单价和新增用户数量的业务数据对象
     */
    @Override
    public BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end) {
        // 初始化参数map，用于后续查询
        Map map = new HashMap();
        map.put("begin", begin);
        map.put("end", end);

        // 查询时间段内所有订单的数量
        Integer totalOrderCount = orderMapper.countByMap(map);
        // 查询时间段内被取消的订单数量，并计算营业额
        map.put("status", Orders.COMPLETED);

        Double turnover = orderMapper.sumByMap(map);
        turnover=turnover == null ? 0.0 : turnover;

        // 计算有效订单数量
        Integer validOrderCount = orderMapper.countByMap(map);

        // 初始化商品单价和订单完成率为0
        Double unitPrice = 0.0;

        Double orderCompletionRate = 0.0;

        // 当总订单数和有效订单数均不为0时，计算订单完成率和商品单价
        if (totalOrderCount != 0 && validOrderCount != 0) {
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
            unitPrice = turnover / validOrderCount;
        }
        // 查询时间段内新增的用户数量
        Integer newUsers = userMapper.countByMap(map);
        // 构建并返回业务数据对象
        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newUsers(newUsers)
                .build();
    }


    @Override
    /**
     * 获取订单概览
     * 此方法通过查询数据库中不同状态的订单数量来提供订单的总体概览
     * 它使用LocalDateTime来限定查询的时间范围，确保只计算当日的订单
     * 订单状态包括：待确认、已确认待发货、已完成和已取消
     * 最后，它返回一个OrderOverViewVO对象，其中包含了各类订单的数量
     */
    public OrderOverViewVO getOrderOverView() {
        // 初始化一个Map来存储查询条件
        Map map = new HashMap();
        // 设置查询的开始时间为当日的开始时间
        map.put("begin", LocalDateTime.now().with(LocalTime.MIN));
        // 设置订单状态为待确认
        map.put("status", Orders.TO_BE_CONFIRMED);

        // 查询并获取待确认状态的订单数量
        Integer waitingOrders = orderMapper.countByMap(map);

        // 更新订单状态为已确认待发货，查询并获取对应订单数量
        map.put("status", Orders.CONFIRMED);
        Integer deliveredOrders = orderMapper.countByMap(map);

        // 更新订单状态为已完成，查询并获取对应订单数量
        map.put("status", Orders.COMPLETED);
        Integer completedOrders = orderMapper.countByMap(map);

        // 更新订单状态为已取消，查询并获取对应订单数量
        map.put("status", Orders.CANCELLED);
        Integer cancelledOrders = orderMapper.countByMap(map);

        // 更新订单状态为null，即查询所有状态的订单数量
        map.put("status", null);
        Integer allOrders = orderMapper.countByMap(map);
        // 使用Builder模式构建并返回订单概览对象
        return OrderOverViewVO.builder()
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders)
                .build();
    }


    /**
     * 获取菜品概览信息
     *
     * @return 包含菜品上架数量和下架数量的DishOverViewVO对象
     */
    @Override
    public DishOverViewVO getDishOverView() {
        // 初始化参数map，用于查询条件的设置
        Map map = new HashMap();
        // 设置查询状态为启用，以统计上架的菜品数量
        map.put("status", StatusConstant.ENABLE);
        // 查询并获取上架菜品的数量
        Integer sold = dishMapper.countByMap(map);

        // 更新查询状态为禁用，以统计下架的菜品数量
        map.put("status", StatusConstant.DISABLE);
        // 查询并获取下架菜品的数量
        Integer discontinued = dishMapper.countByMap(map);
        // 构建并返回菜品概览信息对象
        return DishOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }


    /**
     * 获取套餐概览信息
     *
     * @return 包含售出和停售套餐数量的概览对象
     */
    @Override
    public SetmealOverViewVO getSetmealOverView() {
        // 初始化查询条件的Map
        Map map = new HashMap();
        // 设置查询状态为启用，以查询售出的套餐数量
        map.put("status", StatusConstant.ENABLE);
        // 根据启用状态查询套餐数量
        Integer sold = setmealMapper.countByMap(map);

        // 更新查询状态为停用，以查询停售的套餐数量
        map.put("status", StatusConstant.DISABLE);
        // 根据停用状态查询套餐数量
        Integer discontinued = setmealMapper.countByMap(map);

        // 构建并返回套餐概览信息对象，包含售出和停售数量
        return SetmealOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

}
