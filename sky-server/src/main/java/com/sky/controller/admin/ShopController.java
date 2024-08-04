package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 店铺管理控制器，提供与店铺相关的管理接口。
 */
@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {
    /**
     * 店铺状态的Redis缓存key。
     */
    public static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    /**
     * 设置店铺的营业状态。
     *
     * @param status 店铺的营业状态，1表示营业中，其它值表示打烊中。
     * @return 操作结果。
     */
    @PutMapping("/{status}")
    @ApiOperation("设置店铺的营业状态")
    public Result setStatus(@PathVariable Integer status) {
        // 日志记录当前设置的店铺状态
        log.info("设置店铺的营业状态为：{}", status == 1 ? "营业中" : "打烊中");
        // 将店铺状态存储到Redis，使用SHOP_STATUS作为key
        redisTemplate.opsForValue().set(KEY, status);
        return Result.success();
    }

    /**
     * 获取店铺的营业状态。
     *
     * @return 包含店铺当前营业状态的操作结果。
     */
    @GetMapping("/status")
    @ApiOperation("获取店铺的营业状态")
    public Result<Integer> getStatus() {
        // 从Redis获取店铺状态，使用SHOP_STATUS作为key
        Integer status = redisTemplate.opsForValue().get(KEY);
        // 日志记录获取的店铺状态
        log.info("获取店铺的营业状态为：{}", status == 1 ? "营业中" : "打烊中");
        return Result.success(status);
    }
}