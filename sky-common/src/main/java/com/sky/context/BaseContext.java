package com.sky.context;

/**
 * 基础上下文类，用于在应用程序的执行线程中存储和检索当前请求的上下文信息。
 * 此实现使用了{@link ThreadLocal}来确保线程安全，每个线程可以有自己的独立上下文。
 */
public class BaseContext {

    /**
     * 使用{@link ThreadLocal}来存储当前请求的唯一标识符，例如用户ID。
     * 这允许在多个方法调用之间保持状态，而不需要通过方法参数显式传递。
     */
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置当前请求的上下文标识符。
     *
     * @param id 当前请求的上下文标识符，如用户ID。
     */
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    /**
     * 获取当前请求的上下文标识符。
     *
     * @return 当前请求的上下文标识符，可能为null，如果没有设置过。
     */
    public static Long getCurrentId() {
        return threadLocal.get();
    }

    /**
     * 清除当前请求的上下文标识符，通常在请求结束时调用。
     */
    public static void removeCurrentId() {
        threadLocal.remove();
    }

}