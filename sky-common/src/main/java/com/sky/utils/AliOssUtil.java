/**
 * 阿里云OSS工具类，封装了与阿里云OSS相关的操作。
 * 提供了文件上传的功能。
 */
package com.sky.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.io.ByteArrayInputStream;

/**
 * 用于操作阿里云OSS的实用工具类。
 */
@Data
@AllArgsConstructor
@Slf4j
public class AliOssUtil {

    // 阿里云OSS服务的终端点
    private String endpoint;
    // 阿里云OSS的AccessKeyId
    private String accessKeyId;
    // 阿里云OSS的AccessKeySecret
    private String accessKeySecret;
    // 阿里云OSS的存储桶名称
    private String bucketName;

    /**
     * 将字节数组上传到阿里云OSS。
     *
     * @param bytes       要上传的文件的字节数组。
     * @param objectName  OSS中的文件名。
     * @return 返回文件在OSS上的访问路径。
     */
    public String upload(byte[] bytes, String objectName) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 使用ByteArrayInputStream作为输入流，创建PutObject请求。
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(bytes));
        } catch (OSSException oe) {
            // 捕获OSS服务异常，打印错误信息。
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message: " + oe.getErrorMessage());
            System.out.println("Error Code: " + oe.getErrorCode());
            System.out.println("Request ID: " + oe.getRequestId());
            System.out.println("Host ID: " + oe.getHostId());
        } catch (ClientException ce) {
            // 捕获客户端异常，打印错误信息。
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ce.getMessage());
        } finally {
            // 关闭OSSClient连接。
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        // 构建并返回文件在OSS上的访问路径。
        StringBuilder stringBuilder = new StringBuilder("https://");
        stringBuilder.append(bucketName)
                .append(".")
                .append(endpoint)
                .append("/")
                .append(objectName);

        // 记录日志信息，表明文件上传成功。
        log.info("文件上传到:{}", stringBuilder.toString());

        return stringBuilder.toString();
    }
}