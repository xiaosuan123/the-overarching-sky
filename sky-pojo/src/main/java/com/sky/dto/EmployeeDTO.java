package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * 员工数据传输对象，用于封装员工相关信息的传输数据。
 * 该对象通常用于API请求和响应中，以便于前端获取和操作员工数据。
 */
@Data
public class EmployeeDTO implements Serializable {

    /**
     * 员工的唯一标识符ID。
     */
    private Long id;

    /**
     * 员工的用户名，可能用于登录或系统内标识。
     */
    private String username;

    /**
     * 员工的真实姓名。
     */
    private String name;

    /**
     * 员工的联系电话，可用于工作联系或紧急情况。
     */
    private String phone;

    /**
     * 员工的性别，通常用文字表示，如“男”、“女”。
     */
    private String sex;

    /**
     * 员工的身份证号码，用于身份验证和记录。
     */
    private String idNumber;

}