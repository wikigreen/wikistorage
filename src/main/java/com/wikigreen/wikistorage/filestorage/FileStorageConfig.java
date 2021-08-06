package com.wikigreen.wikistorage.filestorage;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileStorageConfig {

    @Value("${aws.s3.accessKey}")
    private String accessKey;

    @Value("${aws.s3.accessSecret}")
    private String secretKey;

    @Value("${aws.s3.region}")
    private String region;

    @Bean
    public AmazonS3 s3Client(){
        return AmazonS3ClientBuilder.standard().
                withCredentials(
                        new AWSStaticCredentialsProvider(
                                new BasicAWSCredentials(accessKey, secretKey)
                        )
                )
                .withRegion(region)
                .build();
    }

}
