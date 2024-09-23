package ru.shtamov.s3_service.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ObjectStorageConfig {


    @Value("${s3-service.bucket_name}")
    private String bucketName;


    @Value("${s3-service.access_key_id}")
    private String accessKeyId;


    @Value("${s3-service.secret_key}")
    private String secretAccessKey;
}
