package com.sky.constant;

/**
 *  JWT声明中使用的常量，定义了JWT令牌中存储的声明字段的名称。
 * 这些常量用于在生成和解析JWT时保持一致性，确保用户信息的正确传递和验证。
 */
public class JwtClaimsConstant {

    /**
     * JWT声明中员工ID的字段名称。
     * 用于在JWT中存储和检索员工的唯一标识符。
     */
    public static final String EMP_ID = "empId";

    /**
     * JWT声明中用户ID的字段名称。
     * 用于在JWT中存储和检索用户的唯一标识符。
     */
    public static final String USER_ID = "userId";

    /**
     * JWT声明中手机号的字段名称。
     * 用于在JWT中存储和检索用户的手机号码。
     */
    public static final String PHONE = "phone";

    /**
     * JWT声明中用户名的字段名称。
     * 用于在JWT中存储和检索用户的登录名或用户名。
     */
    public static final String USERNAME = "username";

    /**
     * JWT声明中姓名的字段名称。
     * 用于在JWT中存储和检索用户的全名。
     */
    public static final String NAME = "name";

}