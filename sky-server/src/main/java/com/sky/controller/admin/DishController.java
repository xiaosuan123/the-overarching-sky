package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品接口，接收一个菜品数据传输对象，并保存到数据库。
     *
     * @param dishDTO 包含菜品信息的数据传输对象。
     * @return 操作结果，成功返回空结果集。
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO) {
        // 记录日志，包含传入的菜品信息
        log.info("新增菜品：{}", dishDTO);
        // 调用服务层方法保存菜品及其风味信息
        dishService.saveWithFlavor(dishDTO);
        // 根据菜品分类ID清除缓存
        String key = "dish_" + dishDTO.getCategoryId();
        cleanCache(key);
        // 返回操作成功结果
        return Result.success();
    }

    /**
     * 菜品分页查询接口，接收分页查询条件，并返回分页结果。
     *
     * @param dishPageQueryDTO 包含分页查询条件的数据传输对象。
     * @return 包含分页结果的操作结果。
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        // 记录日志，包含传入的分页查询条件
        log.info("菜品分页查询：{}", dishPageQueryDTO);
        // 调用服务层方法执行分页查询
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        // 返回包含分页结果的操作结果
        return Result.success(pageResult);
    }

    /**
     * 菜品批量删除接口，根据传入的ID列表删除相应的菜品记录。
     *
     * @param ids 要删除的菜品ID列表。
     * @return 操作结果，成功返回空结果集。
     */
    @DeleteMapping
    @ApiOperation("菜品批量删除")
    public Result delete(@RequestParam List<Long> ids) {
        // 记录日志，包含传入的菜品ID列表
        log.info("菜品批量删除：{}", ids);
        // 调用服务层方法执行批量删除操作
        dishService.deleteBatch(ids);
        // 清除所有菜品相关的缓存
        cleanCache("dish_*");
        // 返回操作成功结果
        return Result.success();
    }

    /**
     * 根据ID查询菜品接口，根据传入的菜品ID查询菜品的详细信息。
     *
     * @param id 要查询的菜品ID。
     * @return 包含菜品详细信息的操作结果。
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id) {
        // 记录日志，包含传入的菜品ID
        log.info("根据id查询菜品：{}", id);
        // 调用服务层方法获取指定ID的菜品及其风味信息
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        // 返回包含菜品信息的操作结果
        return Result.success(dishVO);
    }

    /**
     * 修改菜品接口，根据传入的菜品数据传输对象更新菜品信息。
     *
     * @param dishDTO 包含更新菜品信息的数据传输对象。
     * @return 操作结果，成功返回空结果集。
     */
    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO) {
        // 记录日志，包含传入的菜品信息
        log.info("修改菜品：{}", dishDTO);
        // 调用服务层方法更新菜品及其风味信息
        dishService.updateWithFlavor(dishDTO);
        // 清除所有菜品相关的缓存
        cleanCache("dish_*");
        // 返回操作成功结果
        return Result.success();
    }

    /**
     * 菜品起售停售接口，根据传入的状态和菜品ID更新菜品的上架状态。
     *
     * @param status 菜品的上架状态，1表示起售，0表示停售。
     * @param id     菜品ID。
     * @return 包含操作结果的泛型结果。
     */
    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售停售")
    public Result<String> startOrStop(@PathVariable Integer status, Long id) {
        // 调用服务层方法更新菜品的上架状态
        dishService.startOrStop(status, id);
        // 清除所有菜品相关的缓存
        cleanCache("dish_*");
        // 返回操作成功结果
        return Result.success();
    }

    /**
     * 根据分类ID查询菜品接口，返回指定分类下的所有菜品列表。
     *
     * @param categoryId 菜品分类的ID，如果为null，则返回所有菜品。
     * @return 包含菜品列表的操作结果。
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list( Long categoryId) {
        // 调用服务层方法，根据分类ID获取菜品列表
        List<Dish> list = dishService.list(categoryId);
        // 返回包含菜品列表的操作结果
        return Result.success(list);
    }

    /**
     * 清除缓存的方法，根据传入的key模式删除匹配的缓存项。
     *
     * @param pattern 缓存的key模式，用于匹配Redis中的keys。
     */
    private void cleanCache(String pattern) {
        // 使用RedisTemplate获取匹配pattern的所有keys
        Set<String> keys = redisTemplate.keys(pattern);
        // 删除匹配的keys
        redisTemplate.delete(keys);
    }
}