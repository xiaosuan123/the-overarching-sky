package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实体类，用于存储和管理用户账户的相关信息。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户的唯一标识符ID。
     */
    private Long id;

    /**
     * 微信用户唯一标识，用于关联微信账户。
     */
    private String openid;

    /**
     * 用户的真实姓名。
     */
    private String name;

    /**
     * 用户的手机号码。
     */
    private String phone;

    /**
     * 用户的性别，通常用"0"表示女性，"1"表示男性。
     */
    private String sex;

    /**
     * 用户的身份证号码。
     */
    private String idNumber;

    /**
     * 用户的头像链接。
     */
    private String avatar;

    /**
     * 用户注册的时间。
     */
    private LocalDateTime createTime;

}