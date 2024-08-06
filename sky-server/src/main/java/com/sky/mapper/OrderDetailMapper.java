/**
 * 订单详情数据访问接口，用于处理与订单详情相关的数据库操作。
 */
package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * OrderDetailMapper接口定义了订单详情相关的数据库操作。
 */
@Mapper
public interface OrderDetailMapper {

    /**
     * 批量插入订单详情记录。
     *
     * @param orderDetailList 要批量插入的订单详情列表
     */
    void insertBatch(List<OrderDetail> orderDetailList);

    /**
     * 根据订单ID查询订单详情。
     *
     * @param orderId 订单ID
     * @return 与订单ID关联的订单详情列表
     */
    @Select("select * from order_detail where order_id=#{orderId}")
    List<OrderDetail> getByOrderId(Long orderId);
}