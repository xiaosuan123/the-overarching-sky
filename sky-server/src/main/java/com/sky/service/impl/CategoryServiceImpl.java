package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 保存分类信息。
     * <p>将传入的分类数据传输对象（CategoryDTO）转换为分类实体（Category），
     * 并设置其状态、创建时间和更新时间等属性，然后调用数据访问层进行数据库插入操作。</p>
     *
     * @param categoryDTO 分类信息数据传输对象
     */
    public void save(CategoryDTO categoryDTO) {
        // 创建Category实体实例
        Category category = new Category();
        // 使用BeanUtils复制属性，从categoryDTO到category实体
        BeanUtils.copyProperties(categoryDTO, category);
        // 设置分类状态为禁用
        category.setStatus(StatusConstant.DISABLE);
        // 设置创建时间为当前时间
        category.setCreateTime(LocalDateTime.now());
        // 设置更新时间为当前时间
        category.setUpdateTime(LocalDateTime.now());
        // 设置创建用户和更新用户为当前登录用户的ID
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateUser(BaseContext.getCurrentId());
        // 调用数据访问层的insert方法，将category实体插入数据库
        categoryMapper.insert(category);
    }


    /**
     * 分页查询分类信息。
     *
     * @param categoryPageQueryDTO 分类分页查询条件数据传输对象
     * @return 分页结果对象，包含总记录数和当前页数据
     */

    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        // 使用PageHelper插件进行分页处理
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        // 调用Mapper接口的pageQuery方法获取分页数据
        Page<Category> page = categoryMapper.pageQuery(categoryPageQueryDTO);
        // 将Page对象转换成PageResult对象并返回
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 根据ID删除分类信息。
     * <p>在删除之前，检查该分类是否被菜品或套餐引用，如果被引用则抛出异常。</p>
     *
     * @param id 分类的唯一标识ID
     */

    public void deleteById(Long id) {
        // 检查该分类ID下是否有对应的菜品
        Integer count = dishMapper.countByCategoryId(id);
        if (count > 0) {
            // 如果有菜品引用了该分类，则不允许删除，并抛出异常
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }
        // 检查该分类ID下是否有对应的套餐
        count = setmealMapper.countByCategoryId(id);
        if (count > 0) {
            // 如果有套餐引用了该分类，则不允许删除，并抛出异常
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }
        // 如果没有被引用，则调用Mapper接口的deleteById方法进行删除
        categoryMapper.deleteById(id);
    }

    /**
     * 更新分类信息。
     *
     * @param categoryDTO 包含更新信息的分类数据传输对象
     */

    public void update(CategoryDTO categoryDTO) {
        // 创建Category实体实例
        Category category = new Category();
        // 使用BeanUtils复制属性，从categoryDTO到category实体
        BeanUtils.copyProperties(categoryDTO, category);
        // 设置更新时间为当前时间
        category.setUpdateTime(LocalDateTime.now());
        // 设置更新用户为当前登录用户ID
        category.setUpdateUser(BaseContext.getCurrentId());
        // 调用Mapper接口的update方法，更新数据库中的分类记录
        categoryMapper.update(category);
    }

    /**
     * 启用或禁用分类。
     *
     * @param status 状态值，例如1表示启用，0表示禁用
     * @param id     分类的唯一标识ID
     */

    public void startOrStop(Integer status, Long id) {
        // 使用建造者模式创建Category实体实例并设置相关属性
        Category category = Category.builder()
                .id(id)
                .status(status)
                .updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId())
                .build();
        // 调用Mapper接口的update方法，更新分类状态
        categoryMapper.update(category);
    }

    /**
     * 根据类型查询分类列表。
     *
     * @param type 分类类型，如果为null，则返回所有类型的分类列表
     * @return 分类列表
     */

    public List<Category> list(Integer type) {
        // 调用Mapper接口的list方法，获取分类列表
        return categoryMapper.list(type);
    }

}
