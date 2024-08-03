package com.sky.constant;

/**
 * 信息提示常量类，用于定义应用程序中使用的各种提示信息。
 * 这些常量用于在用户执行操作时提供明确的状态反馈，例如登录、注册、数据修改等。
 */
public class MessageConstant {

    /**
     * 密码错误提示信息。
     */
    public static final String PASSWORD_ERROR = "密码错误";

    /**
     * 账号不存在提示信息。
     */
    public static final String ACCOUNT_NOT_FOUND = "账号不存在";

    /**
     * 账号被锁定提示信息。
     */
    public static final String ACCOUNT_LOCKED = "账号被锁定";

    /**
     * 未知错误提示信息。
     */
    public static final String UNKNOWN_ERROR = "未知错误";

    /**
     * 用户未登录提示信息。
     */
    public static final String USER_NOT_LOGIN = "用户未登录";

    /**
     * 分类因关联套餐而不能删除的提示信息。
     */
    public static final String CATEGORY_BE_RELATED_BY_SETMEAL = "当前分类关联了套餐,不能删除";

    /**
     * 分类因关联菜品而不能删除的提示信息。
     */
    public static final String CATEGORY_BE_RELATED_BY_DISH = "当前分类关联了菜品,不能删除";

    /**
     * 购物车数据为空时不能下单的提示信息。
     */
    public static final String SHOPPING_CART_IS_NULL = "购物车数据为空，不能下单";

    /**
     * 用户地址为空时不能下单的提示信息。
     */
    public static final String ADDRESS_BOOK_IS_NULL = "用户地址为空，不能下单";

    /**
     * 登录失败提示信息。
     */
    public static final String LOGIN_FAILED = "登录失败";

    /**
     * 文件上传失败提示信息。
     */
    public static final String UPLOAD_FAILED = "文件上传失败";

    /**
     * 套餐因包含未启售菜品而无法启售的提示信息。
     */
    public static final String SETMEAL_ENABLE_FAILED = "套餐内包含未启售菜品，无法启售";

    /**
     * 密码修改失败提示信息。
     */
    public static final String PASSWORD_EDIT_FAILED = "密码修改失败";

    /**
     * 起售中的菜品不能删除的提示信息。
     */
    public static final String DISH_ON_SALE = "起售中的菜品不能删除";

    /**
     * 起售中的套餐不能删除的提示信息。
     */
    public static final String SETMEAL_ON_SALE = "起售中的套餐不能删除";

    /**
     * 当前菜品关联了套餐而不能删除的提示信息。
     */
    public static final String DISH_BE_RELATED_BY_SETMEAL = "当前菜品关联了套餐,不能删除";

    /**
     * 订单状态错误提示信息。
     */
    public static final String ORDER_STATUS_ERROR = "订单状态错误";

    /**
     * 订单不存在提示信息。
     */
    public static final String ORDER_NOT_FOUND = "订单不存在";
    /**
     * 用户已经存在
     */
    public static final String ALREADY_EXISTS = "已存在";
}