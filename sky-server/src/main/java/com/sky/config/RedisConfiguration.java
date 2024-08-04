package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置类，用于创建和配置RedisTemplate Bean。
 */
@Configuration
@Slf4j
public class RedisConfiguration {

    /**
     * 创建RedisTemplate Bean的方法。
     *
     * <p>该方法使用Spring的@Bean注解，将其标记为一个Bean创建方法，Spring容器将调用此方法来创建RedisTemplate的实例。
     * 它将设置连接工厂、键序列化器，并返回配置好的RedisTemplate对象。
     *
     * @param redisConnectionFactory Redis连接工厂，用于创建Redis连接。
     * @return 配置好的RedisTemplate对象。
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        // 日志记录，标记Redis模板对象的创建开始
        log.info("开始创建redis模板对象...");

        // 创建RedisTemplate对象
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();

        // 设置Redis连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 设置键的序列化方式，这里使用StringRedisSerializer序列化字符串键
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // 返回配置好的RedisTemplate对象
        return redisTemplate;
    }
}