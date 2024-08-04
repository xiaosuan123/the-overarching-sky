package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * 用户服务接口，提供用户相关的业务逻辑操作。
 */
public interface UserService {

    /**
     * 微信登录功能，通过微信授权信息进行用户登录。
     *
     * @param userLoginDTO 登录数据传输对象，包含微信登录所需的信息。
     * @return 登录成功的用户实体对象。
     */
    User wxLogin(UserLoginDTO userLoginDTO);
}