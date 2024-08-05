package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * C端分类接口控制器，提供分类信息的查询服务。
 */
@RestController("userCategoryController")
@RequestMapping("/user/category")
@Api(tags = "C端-分类接口")
public class CategoryController {

    /**
     * 注入分类服务，用于执行具体的业务逻辑。
     */
    @Autowired
    private CategoryService categoryService;

    /**
     * 查询分类列表。
     * <p>
     * 此接口根据传入的类型参数，查询对应的分类列表。
     *
     * @param type 分类类型，用于筛选特定类型的分类。
     * @return 分类列表，封装在Result对象中。
     */
    @GetMapping("/list")
    @ApiOperation("查询分类")
    public Result<List<Category>> list(Integer type) {
        List<Category> list = categoryService.list(type); // 调用服务层查询分类列表
        return Result.success(list); // 返回封装的分类列表
    }
}