package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * 用户Mapper接口，用于定义与用户表相关的数据库操作。
 */
@Mapper
public interface UserMapper {

    /**
     * 根据用户的OpenID获取用户信息。
     *
     * @param openId 用户的唯一标识OpenID。
     * @return 返回找到的用户对象，如果没有找到则返回null。
     */
    @Select("select * from user where openid=#{openId}")
    User getByOpenid(String openId);

    /**
     * 向用户表中插入一个新的用户记录。
     *
     * @param user 包含用户信息的对象，其属性将被用于插入操作。
     */
    void insert(User user);

    @Select("select * from user where id = #{id}")
    User getById(Long userId);
    /**
     * 根据动态条件统计用户数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}