package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 用户登录数据传输对象，用于封装C端用户登录时所需的数据。
 * 该对象通常用于API请求中，传递用户登录的凭证，例如获取到的授权码。
 */
@Data
public class UserLoginDTO implements Serializable {

    /**
     * 授权码，用户在登录过程中获取的code，通常通过OAuth流程获得。
     * 该授权码用于获取访问令牌，进而完成用户登录。
     */
    private String code;

}