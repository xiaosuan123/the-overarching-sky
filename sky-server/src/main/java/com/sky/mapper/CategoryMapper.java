package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 分类数据访问层接口，提供对分类数据的操作。
 */
@Mapper
public interface CategoryMapper {

    /**
     * 插入新的分类记录。
     *
     * @param category 包含分类信息的实体对象
     */
    @Insert("insert into category(type, name, sort, status, create_time, update_time, create_user, update_user)" +
            " VALUES" +
            " (#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(Category category);

    /**
     * 执行分类分页查询。
     *
     * @param categoryPageQueryDTO 分页查询条件数据传输对象
     * @return 分页结果封装的分类列表
     */
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 根据ID删除分类记录。
     *
     * @param id 分类的唯一标识ID
     */
    @Delete("delete from category where id=#{id}")
    void deleteById(Long id);

    /**
     * 更新分类记录。
     *
     * @param category 包含更新信息的分类实体对象
     */
    void update(Category category);

    /**
     * 根据类型查询分类列表。
     *
     * @param type 分类类型
     * @return 分类列表
     */
    List<Category> list(Integer type);
}