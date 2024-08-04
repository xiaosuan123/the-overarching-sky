/**
 * 包：com.sky.annotation
 *
 * 描述：定义了一个运行时注解 {@code @AutoFill}，用于标记方法，并指定在执行该方法时
 *        应该自动填充的字段对应的操作类型。
 */
package com.sky.annotation;
import com.sky.enumeration.OperationType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 运行时注解，用于标记方法，指示在执行该方法时应该自动填充的字段对应的操作类型。
 */
@Target(ElementType.METHOD) // 指定注解可以应用于方法
@Retention(RetentionPolicy.RUNTIME) // 指定注解在运行时是可用的
public @interface AutoFill {

    /**
     * 返回与该注解关联的操作类型枚举值。
     *
     * @return 操作类型枚举值
     */
    OperationType value();
}