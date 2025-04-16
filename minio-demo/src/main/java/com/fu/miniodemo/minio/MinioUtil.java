package com.fu.miniodemo.minio;

import io.minio.*;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * minio文件工具类
 */
@Component
public class MinioUtil {
    @Resource
    private MinioConfig minioConfig;

    /**
     * 文件上传
     *
     * @param multipartFile 文件
     */
    @SneakyThrows
    public String upload(MultipartFile multipartFile) {
        PutObjectOptions putObjectOptions = new PutObjectOptions(multipartFile.getSize(), PutObjectOptions.MIN_MULTIPART_SIZE);
        putObjectOptions.setContentType(multipartFile.getContentType());
        DateTimeFormatter yyyy = DateTimeFormatter.ofPattern("yyyy");
        DateTimeFormatter MM = DateTimeFormatter.ofPattern("MM");
        DateTimeFormatter dd = DateTimeFormatter.ofPattern("dd");
        LocalDate localDate = LocalDate.now();//获取当前时间
        String bucketName = localDate.format(yyyy);//以年为存储桶
        String path = localDate.format(MM) + "/" + localDate.format(dd) + "/";//在年存储桶里面创建月日文件夹
        boolean bucketExists = minioConfig.getMinioClient().bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!bucketExists) {//判断存储桶yyyy是否存在，不存在则创建
            minioConfig.getMinioClient().makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            //设置读写权限
            String read_write = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetBucketLocation\",\"s3:ListBucket\",\"s3:ListBucketMultipartUploads\"],\"Resource\":[\"arn:aws:s3:::" + bucketName + "\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:DeleteObject\",\"s3:GetObject\",\"s3:ListMultipartUploadParts\",\"s3:PutObject\",\"s3:AbortMultipartUpload\"],\"Resource\":[\"arn:aws:s3:::" + bucketName + "/*\"]}]}";
            minioConfig.getMinioClient().setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(read_write).build());
        }
        //更换文件名称
        String originalFilename = multipartFile.getOriginalFilename();
        assert originalFilename != null;
        String fileName = path + UUID.randomUUID().toString().replaceAll("-", "") + originalFilename.substring(originalFilename.lastIndexOf("."));
        minioConfig.getMinioClient().putObject(PutObjectArgs.builder().bucket(bucketName).object(fileName).stream(
                        multipartFile.getInputStream(), multipartFile.getSize(), -1)
                .contentType(multipartFile.getContentType())
                .build());
        return bucketName + "/" + fileName;
    }

    /**
     * 文件删除
     *
     * @param bucketName 存储桶名称
     * @param fileName   文件名称
     */
    @SneakyThrows
    public void delete(String bucketName, String fileName) {
        minioConfig.getMinioClient().removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build());
    }

}

