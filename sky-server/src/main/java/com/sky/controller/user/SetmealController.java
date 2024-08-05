package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * C端套餐浏览接口控制器，提供套餐信息的查询服务。
 */
@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Api(tags = "C端-套餐浏览接口")
public class SetmealController {

    /**
     * 注入套餐服务，用于执行具体的业务逻辑。
     */
    @Autowired
    private SetmealService setmealService;

    /**
     * 根据分类ID查询套餐列表。
     * <p>
     * 此接口使用Spring的缓存机制，通过分类ID作为缓存键，
     * 缓存查询结果以提高性能。如果缓存中存在数据，则直接返回缓存中的数据。
     * 否则，将从数据库中查询并缓存结果。
     *
     * @param categoryId 分类ID，用于查询特定分类下的套餐。
     * @return 包含套餐信息的列表，封装在Result对象中。
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询套餐")
    @Cacheable(cacheNames = "setmealCache", key = "#categoryId")
    public Result<List<Setmeal>> list(Long categoryId) {
        Setmeal setmeal = new Setmeal();
        setmeal.setCategoryId(categoryId);
        setmeal.setStatus(StatusConstant.ENABLE); // 只查询启用状态的套餐
        List<Setmeal> list = setmealService.list(setmeal); // 调用服务层查询套餐列表
        return Result.success(list); // 返回封装的套餐列表
    }

    /**
     * 根据套餐ID查询包含的菜品列表。
     * <p>
     * 此接口根据传入的套餐ID，查询该套餐包含的所有菜品项。
     *
     * @param id 套餐ID。
     * @return 包含该套餐所有菜品项的列表，封装在Result对象中。
     */
    @GetMapping("/dish/{id}")
    @ApiOperation("根据套餐id查询包含的菜品列表")
    public Result<List<DishItemVO>> dishList(@PathVariable("id") Long id) {
        List<DishItemVO> list = setmealService.getDishItemById(id); // 调用服务层查询菜品项列表
        return Result.success(list); // 返回封装的菜品项列表
    }
}