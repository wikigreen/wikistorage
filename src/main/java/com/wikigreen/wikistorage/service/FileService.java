package com.wikigreen.wikistorage.service;

import com.wikigreen.wikistorage.model.File;
import com.wikigreen.wikistorage.model.FileStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface FileService {
    File save(File file, MultipartFile multipartFile);

    File getById(Long id);

    List<File> getByUserId(Long id);

    byte[] downloadById(Long id);

    File update(File file);

    void deleteById(Long id);

    void setStatusById(Long id, FileStatus fileStatus);

    List<File> getAll();

    List<File> getByUserNickName(String name);
}
