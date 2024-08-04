package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 切面类，用于自动填充实体对象的公共字段。
 * <p>在执行映射器层的方法之前，根据注解 {@link AutoFill} 的配置，
 * 自动填充创建时间和更新时间等字段。</p>
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 定义一个切入点，用于匹配所有标记了 {@link AutoFill} 注解的映射器层方法。
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill))")
    public void autoFillPointcut() {
    }

    /**
     * 在切入点方法执行之前，进行公共字段的自动填充。
     *
     * @param joinPoint 连接点，代表当前被拦截的方法
     */
    @Before("autoFillPointcut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行公共字段自动填充...");

        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 从方法签名中获取AutoFill注解
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        // 获取操作类型
        OperationType operationType = autoFill.value();

        // 获取方法参数
        Object[] args = joinPoint.getArgs();
        // 判断参数是否为空或长度为0
        if (args == null || args.length == 0) {
            return;
        }

        // 获取实体对象
        Object entity = args[0];
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 获取当前用户ID
        Long currentId = BaseContext.getCurrentId();

        // 根据操作类型，执行不同的字段填充逻辑
        if (operationType == OperationType.INSERT) {
            // 插入操作，填充创建时间和创建用户
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //通过反射为对象属性赋值
                setCreateTime.invoke(entity,now);
                setCreateUser.invoke(entity,currentId);
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (operationType == OperationType.UPDATE) {
            // 更新操作，只填充更新时间和更新用户
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //通过反射为对象属性赋值
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}