package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 套餐控制器，提供HTTP接口以实现套餐的增删改查操作。
 */
@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐。
     * <p>
     * 此POST方法接收一个JSON格式的套餐数据传输对象，调用服务层的保存方法，
     * 并清除指定缓存，最后返回操作结果。
     *
     * @param setmealDTO 包含套餐信息的数据传输对象。
     * @return 操作结果，包含成功或失败的状态信息。
     */
    @PostMapping
    @ApiOperation(value = "新增套餐")
    @CacheEvict(cacheNames = "setmealCache", key = "#setmealDTO.categoryId")
    public Result save(@RequestBody SetmealDTO setmealDTO) {
        setmealService.saveWithDish(setmealDTO); // 调用服务层保存套餐信息
        return Result.success(); // 返回成功结果
    }

    /**
     * 分页查询套餐。
     * <p>
     * 此GET方法接收一个分页查询数据传输对象，调用服务层的分页查询方法，
     * 并将查询结果封装在Result对象中返回。
     *
     * @param setmealPageQueryDTO 分页查询数据传输对象，包含查询条件、页码和每页大小。
     * @return 分页结果，包含总记录数、当前页的数据列表以及成功或失败的状态信息。
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO); // 调用服务层进行分页查询
        return Result.success(pageResult); // 返回包含分页结果的成功结果
    }

    /**
     * 批量删除套餐。
     * <p>
     * 此DELETE方法接收一组套餐ID作为请求参数，调用服务层的批量删除方法，
     * 并清除所有相关的缓存条目，最后返回操作结果。
     *
     * @param ids 要删除的套餐ID列表。
     * @return 操作结果，包含成功或失败的状态信息。
     */
    @DeleteMapping
    @ApiOperation(value = "批量删除套餐")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true) // 清除缓存中的所有条目
    public Result delete(@RequestParam List<Long> ids) {
        setmealService.deleteBatch(ids); // 调用服务层批量删除套餐
        return Result.success(); // 返回成功结果
    }

    /**
     * 根据ID查询套餐及其关联的菜品项。
     * <p>
     * 此GET方法接收一个套餐ID作为路径参数，调用服务层的方法获取套餐及其菜品项的详细信息，
     * 并将结果封装在Result对象中返回。
     *
     * @param id 要查询的套餐ID。
     * @return 包含套餐及其菜品项信息的Result对象，以及成功或失败的状态信息。
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询套餐")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        SetmealVO setmealVO = setmealService.getByIdWithDish(id); // 调用服务层获取套餐详情
        return Result.success(setmealVO); // 返回包含套餐详情的成功结果
    }

    /**
     * 修改套餐信息。
     * <p>
     * 此PUT方法接收一个JSON格式的套餐数据传输对象，包含要修改的套餐信息。
     * 调用服务层的更新方法，并清除所有相关的缓存条目，最后返回操作结果。
     *
     * @param setmealDTO 包含要修改的套餐信息的数据传输对象。
     * @return 操作结果，包含成功或失败的状态信息。
     */
    @PutMapping
    @ApiOperation(value = "修改套餐")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true) // 清除缓存中的所有条目
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        setmealService.update(setmealDTO); // 调用服务层更新套餐信息
        return Result.success(); // 返回成功结果
    }

    /**
     * 套餐起售或停售。
     * <p>
     * 此POST方法接收一个状态参数，用于控制套餐的起售或停售。
     * 调用服务层的方法来更新套餐状态，并清除所有相关的缓存条目，最后返回操作结果。
     *
     * @param status 状态参数，1表示起售，0表示停售。
     * @param id 套餐ID。
     * @return 操作结果，包含成功或失败的状态信息。
     */
    @PostMapping("/status/{status}")
    @ApiOperation(value = "套餐起售停售")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true) // 清除缓存中的所有条目
    public Result startOrStop(@PathVariable Integer status, Long id) {
        setmealService.startOrStop(status, id); // 调用服务层更新套餐的起售或停售状态
        return Result.success(); // 返回成功结果
    }
}