package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import java.util.List;

/**
 * 购物车数据访问接口。
 * 包含购物车信息的增删改查操作。
 */
@Mapper
public interface ShoppingCartMapper {

    /**
     * 根据条件查询购物车列表。
     *
     * @param shoppingCart 包含查询条件的购物车对象。
     * @return 符合条件的购物车列表。
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 根据ID更新购物车中商品的数量。
     *
     * @param shoppingCart 包含更新数量和ID的购物车对象。
     */
    @Update("update shopping_cart set number =#{number} where id =#{id}")
    void updateNumberById(ShoppingCart shoppingCart);

    /**
     * 插入新的购物车记录。
     *
     * @param shoppingCart 包含新记录信息的购物车对象。
     */
    @Insert("insert into shopping_cart (name, user_id, dish_id, setmeal_id, dish_flavor, number, amount, image, create_time) " +
            " values (#{name},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{image},#{createTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * 根据用户ID删除购物车记录。
     *
     * @param userId 用户ID。
     */
    @Delete("delete from shopping_cart where user_id=#{userId}")
    void deleteByUserId(Long userId);

    /**
     * 根据ID删除购物车记录。
     *
     * @param id 要删除的购物车记录的ID。
     */
    @Delete("delete from shopping_cart where id = #{id}")
    void deleteById(Long id);

    /**
     * 批量插入购物车记录。
     *
     * @param shoppingCartList 包含多个购物车记录的列表。
     */
    void insertBatch(List<ShoppingCart> shoppingCartList);
}