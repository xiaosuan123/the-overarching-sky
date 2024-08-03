package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

/**
 * 分类服务接口，提供分类数据的增删改查等业务操作。
 */
public interface CategoryService {

    /**
     * 保存分类信息。
     *
     * @param categoryDTO 分类信息数据传输对象
     */
    void save(CategoryDTO categoryDTO);

    /**
     * 分页查询分类信息。
     *
     * @param categoryPageQueryDTO 分类分页查询条件数据传输对象
     * @return 分页结果对象
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 根据ID删除分类信息。
     *
     * @param id 分类的唯一标识ID
     */
    void deleteById(Long id);

    /**
     * 更新分类信息。
     *
     * @param categoryDTO 包含更新信息的分类数据传输对象
     */
    void update(CategoryDTO categoryDTO);

    /**
     * 启用或禁用分类。
     *
     * @param status 状态值，例如1表示启用，0表示禁用
     * @param id 分类的唯一标识ID
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据类型查询分类列表。
     *
     * @param type 分类类型，如果为null，则不按类型过滤
     * @return 分类列表
     */
    List<Category> list(Integer type);
}