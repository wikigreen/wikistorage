package com.wikigreen.wikistorage.service;

import com.wikigreen.wikistorage.model.Event;
import com.wikigreen.wikistorage.model.EventType;
import com.wikigreen.wikistorage.model.File;
import com.wikigreen.wikistorage.model.FileStatus;
import com.wikigreen.wikistorage.repository.EventRepository;
import com.wikigreen.wikistorage.repository.FileRepository;
import com.wikigreen.wikistorage.repository.UserRepository;
import com.wikigreen.wikistorage.service.exceptions.CustomEntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class FileServiceImpl implements FileService {
    private FileRepository fileRepository;
    private FileStore fileStore;
    private EventRepository eventRepository;
    private UserRepository userRepository;

    @Autowired
    public FileServiceImpl(FileRepository fileRepository,
                           FileStore fileStore,
                           EventRepository eventRepository,
                           UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.fileStore = fileStore;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public File save(File file, MultipartFile multipartFile) {
        File fileWithId = fileRepository.save(file);

        try {
            fileStore.save(
                    file.getOwner().getId(),
                    fileWithId.getId(),
                    multipartFile.getInputStream()
            );
        } catch (IOException exception){
            throw new RuntimeException("Unexpected error while uploading file");
        }

        eventRepository.save(
                new Event(EventType.CREATION, new Date(), file, file.getOwner())
        );
        return fileWithId;

    }

    @Override
    @Transactional
    public File getById(Long id) {
        if (!fileRepository.existsById(id)) {
            throw new CustomEntityNotFoundException(id.toString(), "File", "id");
        }
        return fileRepository.getById(id);
    }

    @Override
    @Transactional
    public List<File> getByUserId(Long id) {
        if (!userRepository.existsById(id)) {
            throw new CustomEntityNotFoundException(id.toString(), "User", "id");
        }
        return fileRepository.findByUserId(id);
    }

    @Override
    @Transactional
    public byte[] downloadById(Long id) {
        if (!fileRepository.existsByIdAndFileStatus(id, FileStatus.ACTIVE)) {
            throw new CustomEntityNotFoundException(id.toString(), "File", "id");
        }
        File file = fileRepository.getById(id);
        try {
            byte[] fileInBytes = fileStore.download(
                    file.getOwner().getId(),
                    file.getId()
            );

            eventRepository.save(new Event(
                    EventType.DOWNLOADING,
                    new Date(),
                    file,
                    file.getOwner())
            );

            return fileInBytes;
        } catch (IOException e) {
            throw new RuntimeException("Unexpected error while downloading file");
        }
    }

    @Override
    @Transactional
    public File update(File file) {
        if (!fileRepository.existsById(file.getId())) {
            throw new CustomEntityNotFoundException(file.getId().toString(), "File", "id");
        }

        if (file.getOwner() != null && !userRepository.existsById(file.getOwner().getId())) {
            throw new CustomEntityNotFoundException(file.getOwner().getId().toString(), "User", "id");
        }

        File oldFile = fileRepository.getById(file.getId());

        if (file.getOwner() == null || oldFile.getOwner().getId().equals(file.getOwner().getId())) {
            file.setOwner(oldFile.getOwner());
        } else {
            file.setOwner(userRepository.getById(file.getOwner().getId()));
        }

        file.setFileStatus(oldFile.getFileStatus());
        file.setUploadDate(oldFile.getUploadDate());
        file.setUpdateDate(new Date());

        eventRepository.save(new Event(
                EventType.UPDATE,
                file.getUpdateDate(),
                oldFile,
                oldFile.getOwner())
        );

        fileRepository.save(file);

        return file;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!fileRepository.existsById(id)) {
            throw new CustomEntityNotFoundException(id.toString(), "File", "id");
        }

        File file = fileRepository.getById(id);
        fileStore.delete(file.getOwner().getId(), id);

        fileRepository.setStatusById(id, FileStatus.DELETED);
        eventRepository.save(new Event(
                EventType.DELETION,
                new Date(),
                file,
                file.getOwner())
        );
    }

    @Override
    @Transactional
    public void setStatusById(Long id, FileStatus fileStatus) {
        if (fileRepository.existsById(id)) {
            throw new CustomEntityNotFoundException(id.toString(), "File", "id");
        }

        File file = fileRepository.getById(id);
        fileRepository.setStatusById(id, fileStatus);
        eventRepository.save(new Event(
                EventType.BAN,
                new Date(),
                file,
                file.getOwner())
        );
    }

    @Override
    @Transactional
    public List<File> getAll() {
        return fileRepository.findByFileStatusNot(FileStatus.DELETED);
    }

    @Override
    @Transactional
    public List<File> getByUserNickName(String name) {
        if (!userRepository.existsByNickName(name)) {
            throw new CustomEntityNotFoundException(name, "User", "nickname");
        }
        return fileRepository.findByUserNickName(name);
    }
}
