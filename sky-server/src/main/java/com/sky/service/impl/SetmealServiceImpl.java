package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 实现套餐服务接口，提供具体的业务实现。
 */
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    private SetmealDishMapper setmealDishMapper;
    private DishMapper dishMapper;


    /**
     * 保存套餐信息并关联菜品。
     * <p>
     * 此方法首先创建一个新的套餐对象，然后将传入的套餐数据传输对象的属性复制到新对象中。
     * 接着，将套餐对象保存到数据库中，并获取新生成的套餐ID。
     * 之后，遍历套餐中包含的所有菜品项，为它们设置套餐ID，并批量保存到数据库中。
     *
     * @param setmealDTO 套餐数据传输对象，包含套餐信息和关联的菜品项。
     */
    @Override
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        // 使用BeanUtils工具类复制属性，避免手动设置
        BeanUtils.copyProperties(setmealDTO, setmeal);
        // 将套餐信息保存到数据库
        setmealMapper.insert(setmeal);
        // 获取保存后生成的套餐ID
        Long setmealId = setmeal.getId();
        // 获取套餐中包含的菜品项列表
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        // 为每个菜品项设置套餐ID
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });
        // 批量保存菜品项到数据库
        setmealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * 分页查询套餐信息。
     * <p>
     * 此方法接收分页查询数据传输对象，从中获取页码和每页大小。
     * 使用PageHelper插件启动分页功能，然后调用mapper层的分页查询方法。
     * 最后，将查询结果封装成PageResult对象返回。
     *
     * @param setmealPageQueryDTO 分页查询数据传输对象，包含查询条件、页码和每页大小。
     * @return 分页结果对象，包含总记录数和当前页的数据列表。
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        // 获取页码和每页大小
        int pageNum = setmealPageQueryDTO.getPage();
        int pageSize = setmealPageQueryDTO.getPageSize();
        // 使用PageHelper插件进行分页处理
        PageHelper.startPage(pageNum, pageSize);
        // 调用mapper层的分页查询方法
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        // 将查询结果封装成PageResult对象
        return new PageResult(page.getTotal(), page.getResult());
    }


    /**
     * 批量删除套餐。
     * <p>
     * 此方法首先遍历传入的ID列表，检查每个套餐的状态。如果套餐处于启用状态，则抛出一个异常，
     * 表示不允许删除正在销售的套餐。如果套餐状态检查通过，则再次遍历ID列表，删除套餐及其关联的菜品项。
     *
     * @param ids 要删除的套餐ID列表。
     * @throws DeletionNotAllowedException 如果尝试删除正在销售的套餐，则抛出此异常。
     */
    @Override
    public void deleteBatch(List<Long> ids) {
        // 检查套餐状态，如果正在销售，则不允许删除
        ids.forEach(id -> {
            Setmeal setmeal = setmealMapper.getById(id);
            if (StatusConstant.ENABLE == setmeal.getStatus()) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });
        // 执行删除操作，包括套餐本身和关联的菜品项
        ids.forEach(setmealId -> {
            setmealMapper.deleteById(setmealId);       // 删除套餐
            setmealDishMapper.deleteByDishId(setmealId); // 删除套餐关联的菜品项
        });
    }

    /**
     * 根据ID获取套餐及其关联的菜品详情。
     * <p>
     * 此方法通过传入的套餐ID，查询套餐及其包含的所有菜品项的详细信息，并返回一个套餐视图对象。
     * 该方法通常用于展示套餐的详细信息，包括套餐本身和其包含的菜品。
     *
     * @param id 套餐ID。
     * @return 包含套餐和菜品信息的套餐视图对象。
     */
    @Override
    public SetmealVO getByIdWithDish(Long id) {
        // 通过ID查询套餐及其关联的菜品项
        SetmealVO setmealVO = setmealMapper.getByIdWithDish(id);
        return setmealVO; // 直接返回查询结果
    }

    /**
     * 更新套餐信息及其关联的菜品项。
     * <p>
     * 此方法首先创建一个新的套餐对象，并将传入的套餐数据传输对象的属性复制到新对象中。
     * 接着，调用mapper层的更新方法来更新数据库中的套餐信息。然后，删除原套餐关联的所有菜品项。
     * 最后，遍历新套餐包含的所有菜品项，为它们设置套餐ID，并批量保存到数据库中。
     *
     * @param setmealDTO 包含更新信息的套餐数据传输对象。
     */
    @Override
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        // 复制属性到新的套餐对象
        BeanUtils.copyProperties(setmealDTO, setmeal);
        // 更新数据库中的套餐信息
        setmealMapper.update(setmeal);
        // 获取套餐ID
        Long setmealId = setmealDTO.getId();
        // 删除原套餐关联的所有菜品项
        setmealDishMapper.deleteByDishId(setmealId);
        // 获取新套餐包含的菜品项列表
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        // 为每个菜品项设置套餐ID
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });
        // 批量保存新关联的菜品项
        setmealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * 开启或停止套餐服务。
     * <p>
     * 此方法根据传入的状态值来开启或停止套餐服务。如果状态值为启用（StatusConstant.ENABLE），
     * 则先检查该套餐下的菜品是否全部启用，如果有菜品未启用，则抛出异常。如果菜品状态检查通过，
     * 则更新套餐的状态为启用。如果状态值为禁用（StatusConstant.DISABLE），则直接更新套餐状态为禁用。
     *
     * @param status 状态值，StatusConstant.ENABLE表示启用，StatusConstant.DISABLE表示禁用。
     * @param id 套餐ID。
     * @throws SetmealEnableFailedException 如果尝试启用套餐，但套餐下的菜品有未启用的情况，则抛出此异常。
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        if (status == StatusConstant.ENABLE) {
            // 获取套餐下的所有菜品
            List<Dish> dishList = dishMapper.getBySetmealId(id);
            if (dishList != null && dishList.size()>0) {
                // 检查所有菜品是否启用
                dishList.forEach(dish -> {
                    if (StatusConstant.DISABLE == dish.getStatus()) {
                        // 如果有菜品未启用，抛出异常
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }
        // 构建套餐对象，只包含ID和状态
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        // 更新套餐状态
        setmealMapper.update(setmeal);
    }

    /**
     * 根据条件查询套餐列表。
     * <p>
     * 此方法接收一个套餐对象，其中包含查询条件。使用这个对象调用mapper层的查询方法，
     * 获取满足条件的套餐列表。
     *
     * @param setmeal 包含查询条件的套餐对象。
     * @return 满足查询条件的套餐列表。
     */
    @Override
    public List<Setmeal> list(Setmeal setmeal) {
        // 调用mapper层的list方法，根据setmeal对象中的条件查询套餐列表
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list; // 直接返回查询结果
    }

    /**
     * 根据套餐ID获取关联的菜品项视图列表。
     * <p>
     * 此方法通过传入的套餐ID，调用mapper层的方法，获取与该套餐关联的所有菜品项的视图对象列表。
     * 这通常用于在前端展示套餐包含的菜品详情。
     *
     * @param id 套餐ID。
     * @return 与套餐ID关联的菜品项视图列表。
     */
    @Override
    public List<DishItemVO> getDishItemById(Long id) {
        // 使用mapper层的getDishItemBySetmealId方法，根据套餐ID获取菜品项视图列表
        return setmealMapper.getDishItemBySetmealId(id);
    }
}