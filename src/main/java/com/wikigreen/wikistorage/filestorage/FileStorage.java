package com.wikigreen.wikistorage.filestorage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;


public interface FileStorage {
    void save(String path,
              String fileName,
              Optional<Map<String, String>> optionalMetadata,
              InputStream inputStream);
    byte[] download(String bucketName, String key) throws IOException;
}
