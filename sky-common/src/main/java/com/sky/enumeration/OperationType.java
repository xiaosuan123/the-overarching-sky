package com.sky.enumeration;

/**
 * 表示数据库操作类型的枚举。
 * 该枚举用于标识对数据库执行的操作类型，例如更新（UPDATE）或插入（INSERT）。
 */
public enum OperationType {

    /**
     * 表示数据库的更新操作。
     * 此枚举值用于标识对已存在数据的修改。
     */
    UPDATE,

    /**
     * 表示数据库的插入操作。
     * 此枚举值用于标识新数据的添加。
     */
    INSERT
}