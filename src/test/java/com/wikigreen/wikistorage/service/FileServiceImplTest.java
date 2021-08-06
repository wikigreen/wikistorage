package com.wikigreen.wikistorage.service;

import com.wikigreen.wikistorage.model.File;
import com.wikigreen.wikistorage.model.FileStatus;
import com.wikigreen.wikistorage.model.User;
import com.wikigreen.wikistorage.repository.EventRepository;
import com.wikigreen.wikistorage.repository.FileRepository;
import com.wikigreen.wikistorage.repository.UserRepository;
import com.wikigreen.wikistorage.service.exceptions.CustomEntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {
    @InjectMocks
    FileServiceImpl fileService;

    @Mock
    private FileRepository fileRepository;

    @Mock
    FileStore fileStore;

    @Mock
    EventRepository eventRepository;

    @Mock
    UserRepository userRepository;

    @Test
    void testGetByIdIfFileDoesNotExist(){
        when(fileRepository.existsById(Mockito.anyLong())).thenReturn(false);

        Assertions.assertThrows(CustomEntityNotFoundException.class, () -> fileService.getById(1L));
    }

    @Test
    void testGetByUserIdWhenUserDoesNotExist(){
        when(userRepository.existsById(Mockito.anyLong())).thenReturn(false);

        Assertions.assertThrows(CustomEntityNotFoundException.class, () -> fileService.getByUserId(1L));
    }

    @Test
    void testDownloadByIdWhenFileDoesNotExist(){
        when(fileRepository.existsByIdAndFileStatus(Mockito.anyLong(), eq(FileStatus.ACTIVE))).thenReturn(false);

        Assertions.assertThrows(CustomEntityNotFoundException.class, () -> fileService.downloadById(1L));
    }

    @Test
    void testUpdateWhenFileDoesNotExist(){
        when(fileRepository.existsById(Mockito.anyLong())).thenReturn(false);

        File file = new File();
        file.setId(1L);

        Assertions.assertThrows(CustomEntityNotFoundException.class, () -> fileService.update(file));
    }

    @Test
    void testUpdateWhenNewUserDoesNotExist() {
        when(fileRepository.existsById(Mockito.anyLong())).thenReturn(true);
        when(userRepository.existsById(Mockito.anyLong())).thenReturn(false);

        File file = new File();
        User user = new User();
        user.setId(1L);
        file.setId(1L);
        file.setOwner(user);

        Assertions.assertThrows(CustomEntityNotFoundException.class, () -> fileService.update(file));
    }

    @Test
    void testUpdateIfUpdateDateIsChanging() {
        when(fileRepository.existsById(Mockito.anyLong())).thenReturn(true);
        when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);

        User user = new User();
        user.setId(1L);
        File oldFile = new File("oldFile.txt", user, FileStatus.ACTIVE, new Date(1627992568000L), new Date(1627992568000L));
        oldFile.setId(1L);
        File newFile = new File("newFile.txt", user, FileStatus.ACTIVE, null, null);
        newFile.setId(1L);
        when(fileRepository.getById(1L)).thenReturn(oldFile);

        File actualFile = fileService.update(newFile);

        Assertions.assertTrue(actualFile.getUploadDate().before(actualFile.getUpdateDate()));
    }

    @Test
    void testDeleteByIdWhenFileDoesNotExist(){
        when(fileRepository.existsById(Mockito.anyLong())).thenReturn(false);

        Assertions.assertThrows(CustomEntityNotFoundException.class, () -> fileService.deleteById(1L));
    }


}
