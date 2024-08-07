package com.sky.service.impl;

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
        map.put("status", Orders.CANCELLED);
        Double turnover = orderMapper.sumByMap(map);

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
    public OrderOverViewVO getOrderOverView() {
        return null;
    }

    @Override
    public DishOverViewVO getDishOverView() {
        return null;
    }

    @Override
    public SetmealOverViewVO getSetmealOverView() {
        return null;
    }
}
