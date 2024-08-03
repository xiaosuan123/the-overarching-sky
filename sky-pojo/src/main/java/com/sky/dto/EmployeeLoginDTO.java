package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * 员工登录数据传输对象，用于封装员工登录时所需的数据。
 * 该对象用于API请求中，帮助系统验证员工的凭证。
 */
@ApiModel(description = "员工登录时传递的数据模型")
@Data
public class EmployeeLoginDTO implements Serializable {

    /**
     * 登录使用的用户名。
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 登录使用的密码。
     * 出于安全考虑，实际应用中密码应通过加密方式传输。
     */
    @ApiModelProperty("密码")
    private String password;

}