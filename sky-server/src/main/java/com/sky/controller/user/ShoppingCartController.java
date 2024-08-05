package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "C端-购物车接口")
public class ShoppingCartController {

    /**
     * 注入购物车服务，用于执行购物车相关的业务逻辑。
     */
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加商品到购物车。
     * <p>
     * 此POST接口接收一个购物车数据传输对象，并将其传递给购物车服务，
     * 以添加商品到当前登录用户的购物车中。
     *
     * @param shoppingCartDTO 包含商品信息的数据传输对象。
     * @return 操作结果，包含成功或失败的状态信息。
     */
    @PostMapping("/add")
    @ApiOperation("添加购物车")
    public Result<Object> add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("添加购物车，商品信息为：{}", shoppingCartDTO);
        shoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 查看当前登录用户的购物车商品列表。
     * <p>
     * 此GET接口调用购物车服务，获取并返回当前登录用户的购物车商品列表。
     *
     * @return 用户购物车中的商品列表。
     */
    @GetMapping("/list")
    @ApiOperation("查看购物车")
    public Result<List<ShoppingCart>> list() {
        List<ShoppingCart> list = shoppingCartService.showShoppingCart();
        return Result.success(list);
    }

    /**
     * 清空当前登录用户的购物车。
     * <p>
     * 此DELETE接口调用购物车服务，清空当前登录用户的购物车。
     *
     * @return 操作结果，包含成功或失败的状态信息。
     */
    @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public Result clean() {
        shoppingCartService.cleanShoppingCart();
        return Result.success();
    }

    /**
     * 从购物车中删除一个商品。
     * <p>
     * 此POST接口接收一个购物车数据传输对象，并调用购物车服务，
     * 以减少购物车中对应商品的数量或删除该商品记录。
     *
     * @param shoppingCartDTO 包含要删除商品信息的数据传输对象。
     * @return 操作结果，包含成功或失败的状态信息。
     */
    @PostMapping("/sub")
    @ApiOperation("删除购物车中一个商品")
    public Result delete(@PathVariable ShoppingCartDTO shoppingCartDTO) {
        log.info("删除购物车中一个商品，商品：{}", shoppingCartDTO);
        shoppingCartService.subShoppingCart(shoppingCartDTO);
        return Result.success();
    }
}