package com.wikigreen.wikistorage.filestorage.impl;

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
public class FileStorageImpl {
    private final AmazonS3 s3;

    @Autowired
    public FileStorageImpl(AmazonS3 s3) {
        this.s3 = s3;
    }

    public void save(String path,
                     String fileName,
                     Optional<Map<String, String>> optionalMetadata,
                     InputStream inputStream) {
        ObjectMetadata metadata = new ObjectMetadata();

        optionalMetadata.ifPresent(map -> {
            if (!map.isEmpty()) {
                map.forEach(metadata::addUserMetadata);
            }
        });

        s3.putObject(path, fileName, inputStream, metadata);
    }

    public byte[] download(String bucketName, String key) throws IOException {
        S3Object object = s3.getObject(bucketName, key);
        return IOUtils.toByteArray(object.getObjectContent());
    }
}
