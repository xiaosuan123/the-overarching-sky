package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务实现类，提供用户相关的业务逻辑操作。
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    /**
     * 微信登录的API接口地址。
     */
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;

    /**
     * 根据微信登录信息进行登录操作。
     *
     * @param userLoginDTO 包含微信登录所需的数据传输对象。
     * @return 登录成功的用户实体对象。
     */
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        // 调用方法获取OpenID
        String openid = getOpenid(userLoginDTO.getCode());
        if (openid == null) {
            // 如果获取OpenID失败，则抛出登录失败异常
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        // 通过OpenID查询用户
        User user = userMapper.getByOpenid(openid);
        if (user == null) {
            // 如果用户不存在，则创建新用户并保存
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        // 返回用户实体对象
        return user;
    }

    /**
     * 使用微信提供的code获取用户的OpenID。
     *
     * @param code 微信登录时获取的code。
     * @return 用户的OpenID。
     */
    private String getOpenid(String code) {
        // 构建请求参数
        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");

        // 发送请求并获取JSON格式的响应
        String json = HttpClientUtil.doGet(WX_LOGIN, map);

        // 解析响应JSON，获取OpenID
        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        return openid;
    }
}