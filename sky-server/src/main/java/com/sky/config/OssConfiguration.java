package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OSS配置类，用于创建和管理阿里云OSS服务的工具类实例。
 */
@Configuration
@Slf4j
public class OssConfiguration {

    /**
     * 创建并配置AliOssUtil的Bean。
     * <p>该Bean用于封装与阿里云OSS服务交互的逻辑。如果Spring的应用上下文中不存在相应的Bean，
     * 则创建一个新的AliOssUtil实例，并使用从配置文件中读取的OSS属性进行配置。</p>
     *
     * @param aliOssProperties 阿里云OSS服务的配置属性
     * @return AliOssUtil的实例
     */
    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {
        // 使用阿里云OSS的配置属性创建AliOssUtil实例
        return new AliOssUtil(
                aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName()
        );
    }
}