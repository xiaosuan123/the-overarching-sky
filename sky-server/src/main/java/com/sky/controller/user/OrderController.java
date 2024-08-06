package com.sky.controller.user;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Api(tags = "用户端订单相关接口")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;
    /**
     * 处理用户下单请求
     *
     * @param ordersSubmitDTO 包含下单所需信息的数据传输对象
     * @return 下单结果，包括订单提交VO
     */
    @RequestMapping("/submit")
    @ApiOperation("用户下单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单，参数为：{}",ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 处理订单支付请求
     *
     * @param ordersPaymentDTO 包含支付所需信息的数据传输对象
     * @return 支付结果，包括预支付交易单信息
     * @throws Exception 如果支付过程中出现错误，则抛出异常
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付，参数为：{}",ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        orderService.paySuccess(ordersPaymentDTO.getOrderNumber());
        return Result.success(orderPaymentVO);
    }

    /**
     * 查询历史订单
     *
     * @param page     当前页码
     * @param pageSize 每页显示的订单数量
     * @param status   订单状态（可选）
     * @return 分页查询结果，包括订单列表和总数等信息
     */
    /**
     * 查询历史订单信息。
     *
     * @param page   当前页码
     * @param pageSize 每页显示的数量
     * @param status 订单状态
     * @return 包含分页结果的 {@link Result} 对象
     */
    @PutMapping("/historyOrders")
    @ApiOperation("历史订单查询")
    public Result<PageResult> page(int page, int pageSize, Integer status){
        PageResult pageResult = orderService.pageQuery4User(page, pageSize, status);
        return Result.success(pageResult);
    }

    /**
     * 获取指定订单的详细信息。
     *
     * @param id 订单的唯一标识
     * @return 包含订单详细信息的 {@link Result} 对象
     */
    @GetMapping("/orderDetail/{id}")
    @ApiOperation("查询订单详情")
    public Result<OrderVO> details(@PathVariable("id") Long id){
        OrderVO orderVO = orderService.details(id);
        return Result.success(orderVO);
    }

    /**
     * 取消指定的订单。
     *
     * @param id 订单的唯一标识
     * @return 表示操作成功的 {@link Result} 对象
     * @throws Exception 如果取消过程中出现错误
     */
    @PutMapping("/cancel/{id}")
    @ApiOperation("取消订单")
    public Result cancel(@PathVariable("id") Long id) throws Exception {
        orderService.userCancelById(id);
        return Result.success();
    }

    /**
     * 处理再来一单请求
     * 该方法通过订单ID重复创建一个相同的订单
     * 使用PathVariable注解获取URL中的订单ID
     * 使用ApiOperation注解描述接口功能
     *
     * @param id 订单ID，用于标识需要重复创建的订单
     * @return 返回Result对象，表示操作结果
     */
    @PostMapping("/repetition/{id}")
    @ApiOperation("再来一单")
    public Result repetition(@PathVariable Long id){
        orderService.repetition(id);
        return Result.success();
    }

    /**
     * 处理客户催单请求
     * 通过HTTP GET请求方式接收前端发送的催单指令
     * 使用@ApiOperation注解标注接口功能，以便在Swagger等API文档生成工具中清晰展示
     *
     * @param id 订单ID，通过@PathVariable注解接收路径中的变量{id}，标识需要催单的具体订单
     * @return 返回Result对象，包含操作结果，此处统一返回成功结果，表示催单操作发起成功
     */
    @GetMapping("/reminder/{id}")
    @ApiOperation("客户催单")
    public Result reminder(@PathVariable("id") Long id){
        orderService.reminder(id);
        return Result.success();
    }

}
