package com.sky.constant;

/**
 * 包含用于自动填充公共字段的相关常量的类。
 * 这些常量通常用于实体类中，以指定创建时间和更新时间等字段的自动填充方法。
 */
public class AutoFillConstant {

    /**
     * 实体类中设置创建时间的方法名称。
     * 用于在实体创建时自动填充创建时间字段。
     */
    public static final String SET_CREATE_TIME = "setCreateTime";

    /**
     * 实体类中设置更新时间的方法名称。
     * 用于在实体更新时自动填充更新时间字段。
     */
    public static final String SET_UPDATE_TIME = "setUpdateTime";

    /**
     * 实体类中设置创建用户的ID的方法名称。
     * 用于在实体创建时自动填充创建用户的ID字段。
     */
    public static final String SET_CREATE_USER = "setCreateUser";

    /**
     * 实体类中设置更新用户的ID的方法名称。
     * 用于在实体更新时自动填充更新用户的ID字段。
     */
    public static final String SET_UPDATE_USER = "setUpdateUser";

}