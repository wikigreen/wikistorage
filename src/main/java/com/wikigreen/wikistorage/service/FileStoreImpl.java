package com.wikigreen.wikistorage.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
public class FileStoreImpl implements FileStore {
    private final AmazonS3 amazonS3;
    private static final String BUCKET_PREFIX = "userid-";

    @Autowired
    public FileStoreImpl(AmazonS3 amazonS3){
        this.amazonS3 = amazonS3;
    }

    @Override
    public void save(Long userId, Long fileId, InputStream inputStream) {
        ObjectMetadata metadata = new ObjectMetadata();

        amazonS3.putObject(BUCKET_PREFIX + userId.toString(), fileId.toString(), inputStream, metadata);
    }

    @Override
    public byte[] download(Long userId, Long fileId) throws IOException {
        S3Object object = amazonS3.getObject(BUCKET_PREFIX + userId.toString(), fileId.toString());
        return IOUtils.toByteArray(object.getObjectContent());
    }

    @Override
    public void delete(Long userId, Long fileId) {
        amazonS3.deleteObject(BUCKET_PREFIX + userId.toString(), fileId.toString());
    }

    @Override
    public void createBucket(Long userId) {
        amazonS3.createBucket(BUCKET_PREFIX + userId.toString());
    }
}
