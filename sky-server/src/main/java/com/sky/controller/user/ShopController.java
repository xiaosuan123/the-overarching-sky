package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 用户端店铺控制器，提供用户相关的店铺接口。
 */
@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {
    /**
     * 店铺状态的Redis缓存key。
     */
    public static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate; // 建议指定RedisTemplate的泛型参数

    /**
     * 获取店铺的营业状态。
     *
     * @return 包含店铺当前营业状态的操作结果。
     */
    @GetMapping("/status")
    @ApiOperation("获取店铺的营业状态")
    public Result<Integer> getStatus() {
        // 从Redis获取店铺状态，使用SHOP_STATUS作为key
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        // 日志记录获取的店铺状态
        log.info("获取店铺的营业状态为：{}", status == 1 ? "营业中" : "打烊中");
        return Result.success(status);
    }
}