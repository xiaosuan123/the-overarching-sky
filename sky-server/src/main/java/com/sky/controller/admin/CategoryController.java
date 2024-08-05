package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category") // 管理后台的类目管理 API
@Api(tags = "分类相关接口") // 该 API 所操作的功能的中文名称
@Slf4j // 引入 lombok 的 @Slf4j 注解，方便日后在控制台打印日志
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类。
     * <p>接收一个分类数据传输对象（CategoryDTO），并调用服务层的save方法进行保存。</p>
     *
     * @param categoryDTO 包含新分类信息的数据传输对象
     * @return 操作结果，成功返回空内容
     */
    @PostMapping
    @ApiOperation("新增分类")
    public Result<String> save(@RequestBody CategoryDTO categoryDTO) {
        log.info("新增分类：{}", categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }

    /**
     * 分页查询分类。
     * <p>接收一个分类分页查询数据传输对象（CategoryPageQueryDTO），并调用服务层的pageQuery方法进行分页查询。</p>
     *
     * @param categoryPageQueryDTO 分类分页查询条件数据传输对象
     * @return 分页结果对象，包含总记录数和当前页数据
     */
    @GetMapping("/page")
    @ApiOperation("分页查询分类")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("分页查询分类：{}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }
    /**
     * 删除分类。
     * <p>根据分类的ID调用服务层的deleteById方法进行删除操作。</p>
     *
     * @param id 分类的唯一标识ID
     * @return 操作结果，成功返回空内容
     */
    @DeleteMapping
    @ApiOperation("删除分类")
    public Result<String> deleteById(Long id) {
        log.info("删除分类：{}", id);
        categoryService.deleteById(id);
        return Result.success();
    }

    /**
     * 修改分类。
     * <p>接收一个包含更新信息的分类数据传输对象（CategoryDTO），并调用服务层的update方法进行更新操作。</p>
     *
     * @param categoryDTO 包含更新分类信息的数据传输对象
     * @return 操作结果，成功返回空内容
     */
    @PutMapping
    @ApiOperation("修改分类")
    public Result<String> update(@RequestBody CategoryDTO categoryDTO) {
        categoryService.update(categoryDTO);
        return Result.success();
    }
    /**
     * 启用或禁用分类。
     * <p>根据传入的状态值和分类ID，调用服务层的startOrStop方法更新分类的状态。</p>
     *
     * @param status 状态值，例如1表示启用，0表示禁用
     * @param id 分类的唯一标识ID
     * @return 操作结果，成功返回空内容
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用分类")
    public Result<String> startOrStop(@PathVariable("status") Integer status,Long id) {
        categoryService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * 根据类型查询分类列表。
     * <p>调用服务层的list方法，根据传入的类型参数查询分类列表。</p>
     *
     * @param type 分类类型，如果为null，则返回所有类型的分类列表
     * @return 分类列表的封装结果
     */
    @GetMapping("/list")
    @ApiOperation("根据类型查询分类")
    public Result<List<Category>> list(Integer type) {
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }
}
