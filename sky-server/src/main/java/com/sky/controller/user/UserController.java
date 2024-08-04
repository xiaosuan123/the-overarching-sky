package com.sky.controller.user;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * C端用户控制器，提供用户相关的HTTP API接口。
 */
@RestController
@RequestMapping("/user/user")
@Api(tags = "C端用户相关接口")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 微信登录接口，接收用户的登录请求，并返回包含用户信息和JWT Token的响应。
     *
     * @param userLoginDTO 登录数据传输对象，包含微信登录所需的信息。
     * @return 包含用户登录视图对象UserLoginVO和JWT Token的操作结果。
     */
    @PostMapping("/login")
    @ApiOperation("微信登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        // 记录用户发起的登录请求日志
        log.info("微信用户登录：{}", userLoginDTO.getCode());

        // 调用服务层方法执行微信登录操作
        User user = userService.wxLogin(userLoginDTO);

        // 构建JWT Token中声明的Map
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());

        // 使用JwtUtil工具类生成JWT Token
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(), claims);

        // 构建用户登录视图对象UserLoginVO
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();

        // 返回包含用户登录视图对象和JWT Token的操作结果
        return Result.success(userLoginVO);
    }
}