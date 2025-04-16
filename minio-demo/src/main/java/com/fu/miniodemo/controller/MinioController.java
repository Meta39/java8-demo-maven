package com.fu.miniodemo.controller;

import com.fu.miniodemo.minio.MinioUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("minio")
public class MinioController {
    @Resource
    private MinioUtil minioUtil;

    /**
     * 文件上传
     * @param file 文件
     */
    @PostMapping("upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        return minioUtil.upload(file);
    }

    /**
     * 删除文件
     * @param bucketName 存储桶名称
     * @param fileName 文件名称
     */
    @DeleteMapping("delete")
    public void delete(@RequestParam("bucketName") String bucketName,@RequestParam("fileName") String fileName) {
        minioUtil.delete(bucketName, fileName);
    }

}

