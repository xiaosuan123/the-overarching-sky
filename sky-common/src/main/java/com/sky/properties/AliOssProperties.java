package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云OSS配置属性类，用于存储与阿里云OSS服务相关的配置信息。
 * 此类通过{@link ConfigurationProperties}注解与application.properties或application.yml中的配置绑定，
 * 并使用{@link Component}注解标识为Spring组件，以便自动装配到Spring的应用上下文中。
 */
@Component
@ConfigurationProperties(prefix = "sky.alioss")
@Data
public class AliOssProperties {

    /**
     * 阿里云OSS服务的终端点，用于指定OSS服务的区域。
     */
    private String endpoint;

    /**
     * 阿里云OSS的AccessKeyId，用于身份验证。
     */
    private String accessKeyId;

    /**
     * 阿里云OSS的AccessKeySecret，与AccessKeyId配合用于身份验证。
     */
    private String accessKeySecret;

    /**
     * 阿里云OSS的存储桶名称，用于标识特定的存储桶。
     */
    private String bucketName;

}