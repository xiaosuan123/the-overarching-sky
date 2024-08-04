package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private SetmealMapper setmealMapper;


    /**
     * 使用Spring事务管理保存菜品及其风味信息。
     *
     * <p>此方法首先创建一个新的菜品对象，然后将传入的{@code DishDTO}对象的属性复制到新创建的菜品对象中。
     * 接着，将菜品对象保存到数据库中。之后，获取新创建菜品的ID，并为每个风味设置菜品ID。
     * 最后，将风味列表批量保存到数据库中。
     *
     * @param dishDTO 包含菜品及其风味信息的数据传输对象。
     */
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        // 创建新的菜品对象
        Dish dish = new Dish();
        // 将dishDTO的属性复制到dish对象中
        BeanUtils.copyProperties(dishDTO, dish);
        // 将dish对象插入到数据库中
        dishMapper.insert(dish);
        // 获取刚插入的菜品的ID
        Long dishId = dish.getId();
        // 获取dishDTO中的风味列表
        List<DishFlavor> flavors = dishDTO.getFlavors();
        // 如果风味列表不为空且至少有一个风味
        if (flavors != null && flavors.size() > 0) {
            // 遍历风味列表，为每个风味设置菜品ID
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            // 批量将风味列表插入到数据库中
            dishFlavorMapper.insertBatch(flavors);
        }
    }


    /**
     * 分页查询菜品信息。
     *
     * <p>使用PageHelper插件进行分页处理，根据传入的分页参数{@code dishPageQueryDTO}，调用Mapper层的分页查询方法。
     * 然后，将查询结果封装成{@code PageResult}对象返回。
     *
     * @param dishPageQueryDTO 包含分页查询条件的数据传输对象。
     * @return 包含分页信息和菜品视图对象列表的分页结果。
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        // 使用PageHelper插件启动分页功能
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        // 调用Mapper层的分页查询方法
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        // 将Page对象的总记录数和结果列表封装成PageResult对象返回
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量删除菜品信息，并确保删除操作的事务性。
     *
     * <p>此方法首先检查传入的菜品ID列表中的每个菜品是否处于启用状态。如果菜品处于启用状态，则抛出异常，阻止删除操作。
     * 然后，查询是否有套餐包含这些菜品，如果有，则同样抛出异常，阻止删除。如果以上检查都通过，则执行删除操作，包括菜品信息和相关的风味信息。
     *
     * @param ids 要删除的菜品ID列表。
     * @throws DeletionNotAllowedException 如果菜品处于启用状态或被套餐引用，则抛出此异常。
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        // 检查每个菜品是否处于启用状态
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        // 检查是否有套餐包含这些菜品
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds != null && !setmealIds.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        // 如果检查通过，则执行删除操作
        for (Long id : ids) {
            // 删除菜品信息
            dishMapper.deleteById(id);
            // 删除与菜品相关的风味信息
            dishFlavorMapper.deleteByDishId(id);
        }
    }


    /**
     * 根据菜品ID获取菜品及其风味信息。
     *
     * <p>此方法首先根据传入的菜品ID从数据库中查询菜品信息。然后，查询与该菜品ID关联的所有风味信息。
     * 接着，创建一个菜品视图对象{@code DishVO}，并将菜品信息复制到该视图对象中。最后，将风味信息设置到视图对象中，并返回。
     *
     * @param id 菜品的唯一标识ID。
     * @return 包含菜品及其风味信息的视图对象{@code DishVO}。
     */

    public DishVO getByIdWithFlavor(Long id) {
        // 根据ID查询菜品信息
        Dish dish = dishMapper.getById(id);
        // 查询与菜品ID关联的风味信息列表
        List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(id);
        // 创建DishVO对象
        DishVO dishVO = new DishVO();
        // 将Dish对象的属性复制到DishVO对象中
        BeanUtils.copyProperties(dish, dishVO);
        // 将风味信息设置到DishVO对象中
        dishVO.setFlavors(dishFlavors);
        // 返回包含菜品及其风味信息的DishVO对象
        return dishVO;
    }

    /**
     * 更新菜品信息及其风味信息。
     *
     * <p>此方法首先创建一个新的菜品对象，并使用传入的{@code dishDTO}对象的属性来填充它。
     * 然后，调用Mapper层的更新方法来更新数据库中的菜品信息。接着，检查传入的风味列表是否存在。
     * 如果存在，先删除与该菜品ID关联的所有旧风味信息，然后将新的风味列表中的每个风味的菜品ID设置为当前菜品的ID，
     * 并将新的风味列表批量插入到数据库中。
     *
     * @param dishDTO 包含更新后的菜品及其风味信息的数据传输对象。
     */
    public void updateWithFlavor(DishDTO dishDTO) {
        // 创建一个新的菜品对象
        Dish dish = new Dish();
        // 将dishDTO的属性复制到dish对象中
        BeanUtils.copyProperties(dishDTO, dish);
        // 更新数据库中的菜品信息
        dishMapper.update(dish);
        // 获取dishDTO中的风味列表
        List<DishFlavor> flavors = dishDTO.getFlavors();
        // 如果风味列表不为空且至少有一个风味
        if (flavors != null && !flavors.isEmpty()) {
            // 删除与当前菜品ID关联的所有旧风味信息
            dishFlavorMapper.deleteByDishId(dish.getId());
            // 遍历风味列表，为每个风味设置菜品ID
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dish.getId());
            });
            // 批量插入新的风味信息到数据库
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 启用或禁用菜品，并在需要时更新相关套餐的状态。
     *
     * <p>此方法首先构建一个菜品对象，并设置其状态。然后，调用Mapper层的更新方法来更新数据库中的菜品状态。
     * 如果菜品被禁用，该方法会检查是否有套餐包含此菜品。如果有，这些套餐的状态也会被禁用。
     *
     * @param status 菜品的新状态，通常1表示启用，0表示禁用。
     * @param id     菜品的唯一标识ID。
     */
    @Transactional
    public void startOrStop(Integer status, Long id) {
        // 构建菜品对象并设置状态和ID
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        // 更新数据库中的菜品状态
        dishMapper.update(dish);
        // 如果菜品被禁用
        if (status == StatusConstant.DISABLE) {
            // 获取包含当前菜品ID的列表
            List<Long> dishIds = new ArrayList<>();
            dishIds.add(id);
            // 查询包含当前菜品ID的套餐ID列表
            List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(dishIds);
            // 如果存在相关套餐
            if (setmealIds != null && !setmealIds.isEmpty()) {
                // 遍历套餐ID列表
                for (Long setmealId : setmealIds) {
                    // 构建套餐对象并设置禁用状态
                    Setmeal setmeal = Setmeal.builder()
                            .id(setmealId)
                            .status(StatusConstant.DISABLE)
                            .build();
                    // 更新数据库中的套餐状态
                    setmealMapper.update(setmeal);
                }
            }
        }
    }

    /**
     * 根据分类ID和启用状态列出所有菜品。
     *
     * <p>此方法构建一个菜品查询对象，并设置分类ID和启用状态。然后，调用Mapper层的列表查询方法，
     * 返回所有处于启用状态且属于指定分类的菜品列表。
     *
     * @param categoryId 分类ID，如果为null，则忽略该条件。
     * @return 处于启用状态的菜品列表。
     */
    public List<Dish> list(Long categoryId) {
        // 构建菜品查询对象并设置分类ID和启用状态
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        // 调用Mapper层的列表查询方法
        return dishMapper.list(dish);
    }

    /**
     * 列出包含风味信息的菜品视图对象列表。
     *
     * <p>此方法首先调用Mapper层的列表查询方法，获取所有符合条件的菜品列表。
     * 然后，对于列表中的每个菜品，创建一个菜品视图对象{@code DishVO}，并将菜品信息复制到该视图对象中。
     * 接着，查询与每个菜品ID关联的风味信息列表，并设置到视图对象中。
     * 最后，返回包含所有菜品及其风味信息的视图对象列表。
     *
     * @param dish 查询条件的菜品对象。
     * @return 包含菜品及其风味信息的视图对象列表。
     */

    public List<DishVO> listWithFlavor(Dish dish) {
        // 调用Mapper层的列表查询方法
        List<Dish> dishList = dishMapper.list(dish);
        // 创建视图对象列表
        List<DishVO> dishVOList = new ArrayList<>();
        for (Dish d : dishList) {
            // 创建菜品视图对象并复制属性
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d, dishVO);
            // 查询与菜品ID关联的风味信息列表
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());
            // 将风味信息设置到视图对象中
            dishVO.setFlavors(flavors);
            // 将包含风味信息的视图对象添加到列表中
            dishVOList.add(dishVO);
        }
        // 返回包含所有菜品及其风味信息的视图对象列表
        return dishVOList;
    }

}