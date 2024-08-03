package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * 管理端通用接口控制器，提供通用功能如文件上传等。
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {
    // 通过自动装配注入阿里云OSS工具类
    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 文件上传接口。
     * <p>接收一个multipart文件，并尝试将其上传到阿里云OSS服务。如果上传成功，返回文件的路径；如果失败，返回错误信息。</p>
     *
     * @param file 要上传的文件
     * @return 操作结果，包含文件路径或错误信息
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传：{}", file);

        try {
            // 获取文件原始名称
            String originalFilename = file.getOriginalFilename();
            // 提取文件扩展名
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 生成唯一的OSS对象名称
            String objectName = UUID.randomUUID().toString() + extension;

            // 调用AliOssUtil的upload方法上传文件，并获取文件路径
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(filePath);
        } catch (IOException e) {
            log.error("文件上传失败：{}", e);
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }
    }
}