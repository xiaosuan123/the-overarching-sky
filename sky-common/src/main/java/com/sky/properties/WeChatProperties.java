package com.sky.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信支付配置属性类，用于存储与微信支付相关的配置信息。
 * 此类通过{@link ConfigurationProperties}注解与application.properties或application.yml中的配置绑定，
 * 并使用{@link Component}注解标识为Spring组件，以便自动装配到Spring的应用上下文中。
 */
@Component
@ConfigurationProperties(prefix = "sky.wechat")
@Data
public class WeChatProperties {

    /**
     * 小程序的appid，用于标识小程序。
     */
    private String appid;

    /**
     * 小程序的秘钥，用于小程序的接口调用。
     */
    private String secret;

    /**
     * 商户号，用于标识微信支付的商户。
     */
    private String mchid;

    /**
     * 商户API证书的证书序列号，用于API调用时的签名验证。
     */
    private String mchSerialNo;

    /**
     * 商户私钥文件路径，用于生成签名和验证签名。
     */
    private String privateKeyFilePath;

    /**
     * 证书解密的密钥，用于解密微信支付平台证书。
     */
    private String apiV3Key;

    /**
     * 平台证书文件路径，微信支付平台证书。
     */
    private String weChatPayCertFilePath;

    /**
     * 支付成功的回调地址，用于接收微信支付成功的通知。
     */
    private String notifyUrl;

    /**
     * 退款成功的回调地址，用于接收退款成功的通知。
     */
    private String refundNotifyUrl;

}