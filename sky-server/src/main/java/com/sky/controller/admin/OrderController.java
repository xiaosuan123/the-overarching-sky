package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单管理
 */
@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "订单管理接口")
public class OrderController {
    @Autowired
    private OrderService orderService;
    /**
     * 条件搜索订单
     *
     * @param ordersPageQueryDTO 订单查询条件封装对象
     * @return 返回订单的分页查询结果
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("订单搜索")
    public Result conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 统计各种状态的订单数量
     *
     * @return 返回各种订单状态的数量统计结果
     */
    @GetMapping("/statistics")
    @ApiOperation("各个状态的订单数量统计")
    public Result<OrderStatisticsVO> statistics(){
        OrderStatisticsVO orderStatisticsVO = orderService.statistics();
        return Result.success(orderStatisticsVO);
    }

    /**
     * 查询订单详情
     *
     * @param id 订单的唯一标识
     * @return 返回订单的详细信息
     */
    @GetMapping("/details")
    @ApiOperation("订单详情")
    public Result<Object> details(@PathVariable("id") Long id){
        OrderVO orderVO=orderService.details(id);
        return Result.success(orderVO);
    }

    /**
     * 接单接口
     * 通过订单确认DTO接收订单信息，并在服务层确认订单
     * @param ordersConfirmDTO 订单确认数据传输对象，包含订单确认所需信息
     * @return 接单操作结果，返回成功结果
     */
    @PutMapping("/confirm")
    @ApiOperation("接单")
    public Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO){
        orderService.confirm(ordersConfirmDTO);
        return Result.success();
    }

    /**
     * 拒单接口
     * 通过订单拒单DTO接收订单信息，并在服务层拒单
     * @param ordersRejectionDTO 订单拒单数据传输对象，包含订单拒单所需信息
     * @return 拒单操作结果，返回成功结果
     * @throws Exception 在拒单过程中可能抛出的异常
     */
    @PutMapping("/rejection")
    @ApiOperation("拒单")
    public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        orderService.rejection(ordersRejectionDTO);
        return Result.success();
    }

    /**
     * 取消订单接口
     * 通过订单取消DTO接收订单信息，并在服务层取消订单
     * @param ordersCancelDTO 订单取消数据传输对象，包含订单取消所需信息
     * @return 取消订单操作结果，返回成功结果
     * @throws Exception 在取消订单过程中可能抛出的异常
     */
    /**
     * 取消指定订单。
     *
     * @param ordersCancelDTO 包含订单取消信息的数据传输对象
     * @return 成功的操作结果
     * @throws Exception 如果处理过程中发生错误
     */
    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) throws Exception {
        orderService.cancel(ordersCancelDTO);
        return Result.success();
    }

    /**
     * 将指定的订单设置为派送状态。
     *
     * @param id 订单的唯一标识符
     * @return 成功的操作结果
     */
    @PutMapping("/delivery/{id}")
    @ApiOperation("派送订单")
    public Result delivery(@PathVariable("id") Long id){
        orderService.delivery(id);
        return Result.success();
    }

    /**
     * 将指定的订单设置为已完成状态。
     *
     * @param id 订单的唯一标识符
     * @return 成功的操作结果
     */
    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public Result complete(@PathVariable("id") Long id){
        orderService.complete(id);
        return Result.success();
    }

}
