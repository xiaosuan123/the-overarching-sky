package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
/**
 * 定时任务类，定时处理订单状态
 */
@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;
    /**
     * 处理超时订单的方法
     */
    @Scheduled(cron = "0 * * * * ? ") //每分钟触发一次
    public void processTimeoutOrder() {
        // 记录定时任务执行时间
        log.info("定时处理超时订单：{}", LocalDateTime.now());
        // 设置一个时间阈值，用于筛选15分钟前的订单
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        // 查询订单状态为待支付且下单时间早于阈值的订单列表
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);
        // 如果订单列表非空且含有订单，则遍历订单列表
        if (ordersList != null && ordersList.size() > 0) {
            for (Orders orders : ordersList) {
                // 将订单状态更新为已取消
                orders.setStatus(Orders.CANCELLED);
                // 设置订单取消的原因
                orders.setCancelReason("订单超时，自动取消");
                // 更新订单的取消时间
                orders.setCancelTime(LocalDateTime.now());
                // 更新数据库中的订单信息
                orderMapper.update(orders);
            }
        }
    }

    /**
     * 定时任务：更新派送中订单的状态为已完成
     * 该方法通过调度任务自动更新系统中符合条件的订单状态，以减少人工操作，提高效率
     * 每天凌晨1点执行一次，查询过去一小时内的派送中订单，并将其状态更新为已完成
     */
    @Scheduled(cron = "0 0 1 * * ? ")
    public void processDeliveryOrder() {
        // 记录当前时间，用于后续查询一小时前的订单
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);

        // 查询派送中状态的订单，其下单时间早于一小时前
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);

        // 如果查询到符合条件的订单列表不为空，则遍历列表，更新每个订单的状态为已完成
        if (ordersList != null && !ordersList.isEmpty()) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.COMPLETED);
            }
        }
    }

}

