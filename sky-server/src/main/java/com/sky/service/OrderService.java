package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

/**
 * 订单服务接口，提供订单相关的业务操作。
 */
public interface OrderService {

    /**
     * 提交订单。
     *
     * @param ordersSubmitDTO 订单提交数据传输对象
     * @return 订单提交视图对象
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付。
     *
     * @param ordersPaymentDTO 订单支付数据传输对象
     * @return 订单支付视图对象
     * @throws Exception 支付过程中可能抛出的异常
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功处理。
     *
     * @param outTradeNo 外部交易号
     */
    void paySuccess(String outTradeNo);

    /**
     * 用户分页查询订单。
     *
     * @param page 当前页码
     * @param pageSize 每页显示数量
     * @param status 订单状态
     * @return 分页结果
     */
    PageResult pageQuery4User(int page, int pageSize, Integer status);

    /**
     * 获取订单详情。
     *
     * @param id 订单ID
     * @return 订单视图对象
     */
    OrderVO details(Long id);

    /**
     * 用户取消订单。
     *
     * @param id 订单ID
     * @throws Exception 取消订单过程中可能抛出的异常
     */
    void userCancelById(Long id) throws Exception;

    /**
     * 订单重复支付处理。
     *
     * @param id 订单ID
     */
    void repetition(Long id);

    /**
     * 条件搜索订单。
     *
     * @param ordersPageQueryDTO 订单分页查询数据传输对象
     * @return 分页结果
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 获取订单统计信息。
     *
     * @return 订单统计视图对象
     */
    OrderStatisticsVO statistics();

    /**
     * 确认订单。
     *
     * @param ordersConfirmDTO 订单确认数据传输对象
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 拒绝订单。
     *
     * @param ordersRejectionDTO 订单拒绝数据传输对象
     * @throws Exception 拒绝订单过程中可能抛出的异常
     */
    void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

    /**
     * 取消订单。
     *
     * @param ordersCancelDTO 订单取消数据传输对象
     * @throws Exception 取消订单过程中可能抛出的异常
     */
    void cancel(OrdersCancelDTO ordersCancelDTO) throws Exception;

    /**
     * 发货操作。
     *
     * @param id 订单ID
     */
    void delivery(Long id);

    /**
     * 完成订单。
     *
     * @param id 订单ID
     */
    void complete(Long id);

    /**
     * 客户催单
     * @param id
     */
    void reminder(Long id);
}