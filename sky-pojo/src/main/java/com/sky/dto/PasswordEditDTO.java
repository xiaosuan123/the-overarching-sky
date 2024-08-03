package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 密码编辑数据传输对象，用于封装员工修改密码时所需的数据。
 * 该对象通常用于API请求中，传递员工的旧密码和新密码。
 */
@Data
public class PasswordEditDTO implements Serializable {

    /**
     * 员工ID，用于唯一标识需要修改密码的员工。
     */
    private Long empId;

    /**
     * 旧密码，员工当前使用的密码，用于验证密码修改的合法性。
     */
    private String oldPassword;

    /**
     * 新密码，员工希望设置的新密码。
     * 出于安全考虑，密码在传输和存储时应进行加密处理。
     */
    private String newPassword;

}