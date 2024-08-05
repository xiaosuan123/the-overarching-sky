package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 购物车服务实现类。
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加商品到购物车。
     * <p>
     * 此方法首先根据传入的购物车数据传输对象创建一个新的购物车实体。
     * 然后检查是否存在相同条件的购物车记录，若存在则增加商品数量，否则将新商品添加到购物车。
     * 如果商品是菜品，则从菜品表中获取名称和图片；如果是套餐，则从套餐表中获取。
     * 最后，如果添加的是新商品，则将该记录保存到数据库。
     *
     * @param shoppingCartDTO 包含添加到购物车的商品信息的数据传输对象。
     */
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId()); // 设置用户ID为当前登录用户的ID

        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if (shoppingCartList != null && shoppingCartList.size() == 1) {
            shoppingCart = shoppingCartList.get(0);
            shoppingCart.setNumber(shoppingCart.getNumber() + 1);
            shoppingCartMapper.updateNumberById(shoppingCart); // 更新数量
        } else {
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null) {
                //本次添加到购物车的是菜品
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            } else {
                //本次添加到购物车的是套餐
                Setmeal setmeal = setmealMapper.getById(shoppingCartDTO.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now()); // 设置创建时间为当前时间
            shoppingCartMapper.insert(shoppingCart); // 插入新记录
        }
    }

    /**
     * 展示用户的购物车商品列表。
     * <p>
     * 根据当前登录用户的ID查询购物车中的商品列表。
     *
     * @return 用户购物车中的商品列表。
     */
    public List<ShoppingCart> showShoppingCart() {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(userId)
                .build();
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        return list;
    }


    /**
     * 清理用户的购物车。
     * <p>
     * 根据当前登录用户的ID删除购物车中的所有记录。
     */
    public void cleanShoppingCart() {
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.deleteByUserId(userId);
    }

    /**
     * 从购物车中减去商品数量或删除商品。
     * <p>
     * 此方法根据传入的购物车数据传输对象和当前用户ID查询购物车中的相应记录。
     * 如果找到记录并且商品数量大于1，则减少商品数量。如果商品数量为1，则从购物车中删除该商品记录。
     *
     * @param shoppingCartDTO 包含购物车商品信息的数据传输对象。
     */
    public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId()); // 设置购物车记录的用户ID为当前登录用户ID
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart); // 根据条件查询购物车记录列表
        if (list != null && !list.isEmpty()) {
            shoppingCart = list.get(0); // 获取列表中的第一条记录
            Integer number = shoppingCart.getNumber(); // 获取商品数量
            if (number == 1) {
                // 如果商品数量为1，则从数据库中删除该购物车记录
                shoppingCartMapper.deleteById(shoppingCart.getId());
            } else {
                // 如果商品数量大于1，则减少商品数量
                shoppingCart.setNumber(shoppingCart.getNumber() - 1);
                shoppingCartMapper.updateNumberById(shoppingCart);
            }
        }
        // 注意：如果查询结果为空，即没有找到对应的购物车记录，则不执行任何操作。
    }
}