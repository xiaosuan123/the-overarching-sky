package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 员工实体类，用于存储和管理员工的相关信息。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 员工的唯一标识符ID。
     */
    private Long id;

    /**
     * 员工的用户名，用于登录系统。
     */
    private String username;

    /**
     * 员工的真实姓名。
     */
    private String name;

    /**
     * 员工的密码，出于安全考虑，实际应用中应进行加密存储。
     */
    private String password;

    /**
     * 员工的联系电话。
     */
    private String phone;

    /**
     * 员工的性别。
     */
    private String sex;

    /**
     * 员工的身份证号码。
     */
    private String idNumber;

    /**
     * 员工的状态，例如：0可能表示离职，1表示在职等，具体取值根据业务需求定义。
     */
    private Integer status;

    /**
     * 员工信息的创建时间。注：实际应用中可能需要使用@JsonFormat注解来指定日期时间的格式。
     */
    private LocalDateTime createTime;

    /**
     * 员工信息的最后更新时间。注：实际应用中可能需要使用@JsonFormat注解来指定日期时间的格式。
     */
    private LocalDateTime updateTime;

    /**
     * 创建此员工记录的用户ID。
     */
    private Long createUser;

    /**
     * 最后更新此员工记录的用户ID。
     */
    private Long updateUser;

}