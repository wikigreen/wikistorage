package com.wikigreen.wikistorage.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

public interface FileStore {
    void save(Long userId, Long fileId, InputStream inputStream);
    byte[] download(Long userId, Long fileId) throws IOException;
    void delete(Long userId, Long fileId);
    void createBucket(Long id);
}
