package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.HttpClientUtil;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.websocket.WebSocketServer;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;
    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 提交订单
     *
     * @param ordersSubmitDTO 订单提交数据传输对象，包含订单相关信息
     * @return 返回订单提交结果对象，包含订单时间、订单号和订单金额
     * <p>
     * 此方法主要负责处理订单的提交过程，包括地址验证、购物车校验、订单信息设置、订单明细处理以及购物车数据的清理
     */
    @Override
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        // 根据传入的地址簿ID查询地址信息
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        // 如果地址信息为空，则抛出地址簿异常
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        // 获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        // 初始化购物车对象，用于查询当前用户的购物车列表
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        // 查询当前用户的购物车列表
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        // 如果购物车列表为空，则抛出购物车异常
        if (shoppingCartList == null || shoppingCartList.size() == 0) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // 初始化订单对象，并从订单提交DTO中复制属性
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        // 设置订单时间
        orders.setOrderTime(LocalDateTime.now());
        // 设置订单支付状态为未支付
        orders.setPayStatus(Orders.UN_PAID);
        // 设置订单状态为待支付
        orders.setStatus(Orders.PENDING_PAYMENT);
        // 设置订单号为当前时间戳的字符串形式
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        // 设置订单的收货地址、电话、收货人等信息
        orders.setAddress(addressBook.getDetail());
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setUserId(userId);
        // 插入订单数据到数据库
        orderMapper.insert(orders);

        // 初始化订单明细列表
        List<OrderDetail> orderDetailList = new ArrayList<>();
        // 遍历购物车列表，生成订单明细
        for (ShoppingCart cart : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailList.add(orderDetail);
        }
        // 批量插入订单明细数据到数据库
        orderDetailMapper.insertBatch(orderDetailList);
        // 删除当前用户的购物车数据
        shoppingCartMapper.deleteByUserId(userId);

        // 构建并返回订单提交结果对象
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .orderTime(orders.getOrderTime())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .build();
        return orderSubmitVO;
    }


    // 通过@Value注解读取配置文件中的商店地址
    @Value("${sky.shop.address}")
    private String shopAddress;

    // 通过@Value注解读取配置文件中的百度地图AK，用于地图相关功能
    @Value("${sky.baidu.ak}")
    private String ak;

    /**
     * 检查给定的地址是否超出配送范围
     * 该方法首先解析店铺地址和用户收货地址，然后计算两者之间的距离，如果距离超过设定的阈值（5000米），则抛出异常
     *
     * @param address 用户的收货地址
     * @throws OrderBusinessException 如果店铺地址解析失败、收货地址解析失败或配送路线规划失败，或者配送距离超出范围
     */
    private void checkOutOfRange(String address) {
        // 初始化请求参数，包括店铺地址、输出格式和百度地图的AK
        Map map = new HashMap();
        map.put("address", shopAddress);
        map.put("output", "json");
        map.put("ak", ak);

        // 调用百度地图API解析店铺地址，并处理返回的坐标信息
        String shopCoordinate = HttpClientUtil.doGet("https://api.map.baidu.com/geocoding/v3", map);
        JSONObject jsonObject = JSON.parseObject(shopCoordinate);
        if (!jsonObject.getString("status").equals("0")) {
            throw new OrderBusinessException("店铺地址解析失败");
        }
        JSONObject location = jsonObject.getJSONObject("result").getJSONObject("location");
        String lat = location.getString("lat");
        String lng = location.getString("lng");
        String shopLngLat = lat + "," + lng;

        // 将收货地址加入请求参数，并调用百度地图API解析收货地址
        map.put("address", address);
        String userCoordinate = HttpClientUtil.doGet("https://api.map.baidu.com/geocoding/v3", map);
        jsonObject = JSON.parseObject(userCoordinate);
        if (!jsonObject.getString("status").equals("0")) {
            throw new OrderBusinessException("收货地址解析失败");
        }
        location = jsonObject.getJSONObject("result").getJSONObject("location");
        lat = jsonObject.getString("lat");
        lng = jsonObject.getString("lng");
        String userLngLat = lat + "," + lng;

        // 设置起始点和目的地坐标，以及步骤信息，为调用路线规划API做准备
        map.put("origin", shopLngLat);
        map.put("destination", userLngLat);
        map.put("steps_info", "0");

        // 调用百度地图API进行驾车路线规划，并处理返回的路线信息
        String json = HttpClientUtil.doGet("https://api.map.baidu.com/directionlite/v1/driving", map);
        jsonObject = JSON.parseObject(json);
        if (!jsonObject.getString("status").equals("0")) {
            throw new OrderBusinessException("配送路线规划失败");
        }
        JSONObject result = jsonObject.getJSONObject("result");
        JSONArray jsonArray = (JSONArray) result.get("routers");
        Integer distance = (Integer) ((JSONObject) jsonArray.get(0)).get("distance");

        // 检查配送距离是否超过设定的阈值（5000米），如果超出，则抛出异常
        if (distance > 5000) {
            throw new OrderBusinessException("超出配送范围");
        }
    }


    /**
     * 执行订单支付操作
     * 此方法通过调用微信支付接口，尝试对指定订单进行支付处理
     * 如果订单已被支付，则抛出异常提示
     *
     * @param ordersPaymentDTO 包含订单信息和支付相关信息的DTO，用于指定需要支付的订单
     * @return 返回支付过程中所需的信息，包括微信支付返回的数据和处理结果
     * @throws Exception              可能抛出业务逻辑中未直接处理的异常，如微信支付接口调用失败等
     * @throws OrderBusinessException 如果订单已经被支付，则抛出此异常
     */
    @Override
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        Orders byNumber=orderMapper.getByNumber(ordersPaymentDTO.getOrderNumber());
        byNumber.setStatus(Orders.TO_BE_CONFIRMED);
        // 根据用户ID查询用户信息
        //User user = userMapper.getById(userId);

        // 调用微信支付接口，发起支付请求
       /* JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(),
                new BigDecimal(0.01),
                "苍穹外卖订单",
                user.getOpenid()
        );*/
        JSONObject jsonObject=new JSONObject();
        // 检查微信支付返回的状态，如果订单已被支付，则抛出异常
        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }
        // 将微信支付返回的JSON对象转换为OrderPaymentVO对象，并设置必要的属性
        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));
        orderMapper.update(byNumber);
        // 返回支付相关信息
        return vo;
       /* paySuccess(ordersPaymentDTO.getOrderNumber());
        String orderNumber=ordersPaymentDTO.getOrderNumber();
        Long orderId=orderMapper.getoderId(orderNumber);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code","ORDERPAID");
        OrderPaymentVO vo=jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));
        Integer OrderPaidStatus=Orders.PAID;
        Integer OrderStatus=Orders.TO_BE_CONFIRMED;
        LocalDateTime check_out_time=LocalDateTime.now();
        orderMapper.updateStatus(OrderStatus,OrderPaidStatus,check_out_time,orderId);
        return vo;*/
    }


    /**
     * 订单支付成功处理方法
     *
     * @param outTradeNo 商户订单号，用于标识和查询具体的订单
     *                   - 通过此参数，系统能够查找到对应的订单并更新订单状态
     *                   - 此信息由支付系统提供，用于后续操作中标识订单
     */
    @Override
    public void paySuccess(String outTradeNo) {
        // 获取当前用户ID，用于订单查询和验证
        Long userId = BaseContext.getCurrentId();

        // 根据商户订单号和用户ID查询订单，以确保操作的订单是当前用户的
        Orders ordersDB = orderMapper.getByNumberAndUserId(outTradeNo, userId);

        // 构建并更新订单状态为已支付，更新结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();
        orderMapper.update(orders);

        // 构建通知消息，包括订单类型、订单ID和订单号
        Map map = new HashMap();
        map.put("type", "1");
        map.put("orderId", ordersDB.getId());
        map.put("content", "订单号：" + outTradeNo);

        // 将通知消息转换为JSON格式字符串
        String json = JSON.toJSONString(map);

        // 向所有客户端广播支付成功消息
        webSocketServer.sendToAllClient(json);
    }


    /**
     * 根据用户ID和订单状态分页查询订单信息
     *
     * @param pageNum  页码数
     * @param pageSize 每页显示数量
     * @param status   订单状态
     * @return 返回分页查询结果，包含订单总数和订单列表
     */
    @Override
    public PageResult pageQuery4User(int pageNum, int pageSize, Integer status) {
        // 开始分页查询，设置当前页码和每页显示数量
        PageHelper.startPage(pageNum, pageSize);
        // 创建分页查询参数对象
        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        // 设置用户ID，用于查询当前用户的订单
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        // 设置订单状态，用于筛选符合条件的订单
        ordersPageQueryDTO.setStatus(status);

        // 执行分页查询，获取订单分页数据
        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);
        // 创建一个列表，用于存储转换后的订单VO
        List<OrderVO> list = new ArrayList();
        // 如果查询结果不为空且订单总数大于0
        if (page != null && page.getTotal() > 0) {
            // 遍历订单数据，进行转换
            for (Orders orders : page) {
                // 获取订单ID
                Long orderId = orders.getId();
                // 根据订单ID查询订单详情
                List<OrderDetail> ordersDetails = orderDetailMapper.getByOrderId(orderId);

                // 创建订单VO对象，用于展示订单信息
                OrderVO orderVO = new OrderVO();
                // 将订单数据转换到订单VO中
                BeanUtils.copyProperties(orders, orderVO);
                // 设置订单详情列表
                orderVO.setOrderDetailList(ordersDetails);
                // 将转换后的订单VO添加到列表中
                list.add(orderVO);
            }
        }
        // 返回分页查询结果，包括订单总数和转换后的订单列表
        return new PageResult(page.getTotal(), list);
    }


    /**
     * 根据订单ID获取订单详细信息
     * 此方法通过接收订单ID，查询数据库中对应的订单信息和订单明细，并将其封装为一个VO对象返回
     * 它是与外部交互获取特定订单详细信息的主要入口
     *
     * @param id 订单ID，用于标识需要查询的具体订单
     * @return OrderVO 包含订单基本信息和订单明细的值对象
     */
    @Override
    public OrderVO details(Long id) {
        // 根据订单ID查询订单基本信息
        Orders orders = orderMapper.getById(id);
        // 查询该订单的所有订单明细
        List<OrderDetail> ordersDetails = orderDetailMapper.getByOrderId(orders.getId());
        // 创建一个订单值对象，用于封装订单信息和订单明细
        OrderVO orderVO = new OrderVO();
        // 将订单基本信息复制到值对象中
        BeanUtils.copyProperties(orders, orderVO);
        // 设置订单明细列表到值对象中
        orderVO.setOrderDetailList(ordersDetails);
        // 返回包含订单信息和订单明细的值对象
        return orderVO;
    }


    /**
     * 根据ID取消用户订单
     * 此方法首先检查订单是否存在，如果不存在抛出异常
     * 然后检查订单状态，如果状态不允许取消，则抛出异常
     * 如果订单可以取消，根据订单状态进行不同的处理：
     * - 如果订单状态为待确认，则进行退款操作
     * 最后，更新订单状态为已取消，并记录取消原因和时间
     *
     * @param id 订单ID
     * @throws Exception 抛出异常，包括OrderBusinessException和其他可能的异常
     */
    @Override
    public void userCancelById(Long id) throws Exception {
        // 通过ID查询数据库中的订单信息
        Orders ordersDB = orderMapper.getById(id);
        // 如果订单不存在，则抛出订单未找到的异常
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        // 如果订单状态不允许取消（大于2，假设2为已支付状态），则抛出订单状态错误的异常
        if (ordersDB.getStatus() > 2) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        // 创建一个新的Orders对象用于更新订单信息
        Orders orders = new Orders();
        orders.setId(id);
        // 如果订单状态为待确认，则进行退款操作
        if (ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            // 调用微信支付工具进行退款，参数包括商户订单号、商户退款单号、退款金额和原订单金额
            weChatPayUtil.refund(
                    ordersDB.getNumber(), //商户订单号
                    ordersDB.getNumber(), //商户退款单号
                    new BigDecimal(0.01),//退款金额，单位 元
                    new BigDecimal(0.01));//原订单金额
            // 设置订单支付状态为退款
            orders.setPayStatus(Orders.REFUND);
        }
        // 更新订单状态为已取消，设置取消原因，并更新订单时间
        orders.setPayStatus(Orders.CANCELLED);
        orders.setCancelReason("用户取消");
        orders.setOrderTime(LocalDateTime.now());
        // 更新数据库中的订单信息
        orderMapper.update(orders);
    }

    /**
     * 根据订单ID重复购买
     * 该方法通过订单ID获取订单详情，然后将订单详情转换为购物车项，并插入数据库
     * 主要解决用户重复购买之前订单的问题，简化用户购买流程
     *
     * @param id 订单ID，用于查询特定订单的详细信息
     */
    @Override
    public void repetition(Long id) {
        // 获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        // 通过订单ID获取订单详情
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);
        // 将订单详情转换为购物车列表
        List<ShoppingCart> shoppingCartList = orderDetailList.stream().map(x -> {
            ShoppingCart shoppingCart = new ShoppingCart();
            // 复制订单详情属性到购物车对象，忽略ID字段
            BeanUtils.copyProperties(x, shoppingCart, "id");
            // 设置购物车的用户ID和创建时间
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(LocalDateTime.now());
            return shoppingCart;
        }).collect(Collectors.toList());
        // 批量插入购物车列表到数据库
        shoppingCartMapper.insertBatch(shoppingCartList);
    }


    /**
     * 根据条件搜索订单分页数据
     *
     * @param ordersPageQueryDTO 包含查询条件的分页对象
     * @return 包含订单数据和总数的分页结果
     */
    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        // 开始分页查询，设置页码和每页数量
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        // 执行分页查询并获取订单数据
        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);
        // 将订单数据转换为前端展示的VO列表
        List<OrderVO> orderVOList = getOrderVOList(page);
        // 返回分页结果，包括总数和数据列表
        return new PageResult(page.getTotal(), orderVOList);
    }

    /**
     * 将Page对象中的订单数据转换为OrderVO列表
     *
     * @param page 分页查询结果
     * @return 转换后的OrderVO列表
     */
    private List<OrderVO> getOrderVOList(Page<Orders> page) {
        List<OrderVO> orderVOList = new ArrayList<>();
        // 获取订单列表
        List<Orders> ordersList = page.getResult();
        // 如果订单列表不为空，则进行转换
        if (!CollectionUtils.isEmpty(ordersList)) {
            for (Orders orders : ordersList) {
                OrderVO orderVO = new OrderVO();
                // 将订单数据复制到VO
                BeanUtils.copyProperties(orders, orderVO);
                // 获取订单菜品字符串
                String orderDishes = getOrderDishesStr(orders);
                orderVO.setOrderDishes(orderDishes);
                orderVOList.add(orderVO);
            }
        }
        return orderVOList;
    }

    /**
     * 根据订单ID获取订单菜品详情字符串
     *
     * @param orders 订单对象
     * @return 菜品名称和数量的组合字符串
     */
    private String getOrderDishesStr(Orders orders) {
        // 根据订单ID查询订单详情列表
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());
        // 使用流处理转换为菜品字符串列表
        List<String> orderDishList = orderDetailList.stream().map(x -> {
            String orderDish = x.getName() + "*" + x.getNumber() + ";";
            return orderDish;
        }).collect(Collectors.toList());
        // 合并列表为单一字符串
        return String.join("", orderDishList);
    }


    /**
     * 统计订单状态
     * 该方法通过查询数据库中不同状态的订单数量来生成订单统计信息
     *
     * @return 返回一个包含待确认、已确认和配送中订单数量的实体对象
     */
    @Override
    public OrderStatisticsVO statistics() {
        // 查询待确认状态的订单数量
        Integer toBeConfirmed = orderMapper.countStatus(Orders.TO_BE_CONFIRMED);
        // 查询已确认状态的订单数量
        Integer confirmed = orderMapper.countStatus(Orders.CONFIRMED);
        // 查询配送中状态的订单数量
        Integer deliveryInProgress = orderMapper.countStatus(Orders.DELIVERY_IN_PROGRESS);

        // 创建并填充订单统计实体
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(toBeConfirmed);
        orderStatisticsVO.setConfirmed(confirmed);
        orderStatisticsVO.setDeliveryInProgress(deliveryInProgress);
        return orderStatisticsVO;
    }

    /**
     * 确认订单
     * 该方法将订单状态更新为已确认
     *
     * @param ordersConfirmDTO 包含待确认订单信息的数据传输对象
     */
    @Override
    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
        // 构建并更新订单状态为已确认
        Orders orders = Orders.builder()
                .id(ordersConfirmDTO.getId())
                .status(Orders.CONFIRMED)
                .build();
        orderMapper.update(orders);
    }

    /**
     * 拒绝订单
     * 该方法处理订单拒绝逻辑，包括退款和更新订单状态
     *
     * @param ordersRejectionDTO 包含订单拒绝信息的数据传输对象
     * @throws Exception 如果订单状态或支付状态不符合预期，抛出业务异常
     */
    @Override
    public void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        // 从数据库中获取订单信息
        Orders ordersDB = orderMapper.getById(ordersRejectionDTO.getId());
        // 验证订单是否存在且状态为待确认
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        // 检查订单支付状态
        Integer payStatus = ordersDB.getPayStatus();
        if (payStatus.equals(Orders.PAID)) {
            // 申请退款
            String refund = weChatPayUtil.refund(
                    ordersDB.getNumber(),
                    ordersDB.getNumber(),
                    new BigDecimal(0.01),
                    new BigDecimal(0.01));
            log.info("申请退款：{}", refund);
        }
        // 更新订单状态为已取消，并记录拒绝原因和取消时间
        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setRejectionReason(ordersRejectionDTO.getRejectionReason());
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * 取消订单
     *
     * @param ordersCancelDTO 订单取消信息，包含订单ID和取消原因
     * @throws Exception 可能抛出的异常
     */
    @Override
    public void cancel(OrdersCancelDTO ordersCancelDTO) throws Exception {
        // 通过ID获取订单信息
        Orders ordersDB = orderMapper.getById(ordersCancelDTO.getId());

        // 判断订单支付状态
        Integer payStatus = ordersDB.getPayStatus();
        if (payStatus == 1) {
            // 如果已支付，则申请退款
            String refund = weChatPayUtil.refund(
                    ordersDB.getNumber(),
                    ordersDB.getNumber(),
                    new BigDecimal(0.01),
                    new BigDecimal(0.01));
            log.info("申请退款：{}", refund);
        }
        // 更新订单状态为取消
        Orders orders = new Orders();
        orders.setId(ordersCancelDTO.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason(ordersCancelDTO.getCancelReason());
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * 发货订单
     *
     * @param id 订单ID
     * @throws OrderBusinessException 如果订单不存在或状态不正确
     */
    @Override
    public void delivery(Long id) {
        // 通过ID获取订单信息
        Orders orderDB = orderMapper.getById(id);
        // 检查订单是否存在且状态是否正确
        if (orderDB == null || !orderDB.getStatus().equals(Orders.CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        // 更新订单状态为发货中
        Orders orders = new Orders();
        orders.setId(id);
        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);
        orderMapper.update(orders);
    }

    /**
     * 完成订单
     *
     * @param id 订单ID
     * @throws OrderBusinessException 如果订单不存在或状态不正确
     */
    @Override
    public void complete(Long id) {
        // 通过ID获取订单信息
        Orders orderDB = orderMapper.getById(id);
        // 检查订单是否存在且状态是否正确
        if (orderDB == null || !orderDB.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        // 更新订单状态为已完成
        Orders orders = new Orders();
        orders.setId(id);
        orders.setStatus(Orders.COMPLETED);
        orders.setDeliveryTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * 提醒订单
     *
     * @param id 订单ID
     * @throws OrderBusinessException 如果订单不存在
     */
    @Override
    public void reminder(Long id) {
        // 通过ID获取订单信息
        Orders ordersDB = orderMapper.getById(id);
        // 检查订单是否存在
        if (ordersDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        // 构建提醒消息并发送给所有客户端
        Map map = new HashMap();
        map.put("type", 2);
        map.put("orderId", id);
        map.put("content", "订单号" + ordersDB.getNumber());
        webSocketServer.sendToAllClient(JSON.toJSONString(map));
    }
}

