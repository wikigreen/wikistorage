package com.wikigreen.wikistorage.rest;

import com.wikigreen.wikistorage.dto.FileDto;
import com.wikigreen.wikistorage.dto.FileUpdateDto;
import com.wikigreen.wikistorage.model.File;
import com.wikigreen.wikistorage.model.FileStatus;
import com.wikigreen.wikistorage.model.User;
import com.wikigreen.wikistorage.service.FileService;
import com.wikigreen.wikistorage.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/profile/files")
public class UserFilesRestControllerV1 {
    private FileService fileService;
    private UserService userService;
    private ModelMapper modelMapper;

    @Autowired
    public UserFilesRestControllerV1(FileService fileService, ModelMapper modelMapper, UserService userService) {
        this.fileService = fileService;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<FileDto>> getAllFiles(Principal principal) {
        List<File> files = fileService.getByUserNickName(principal.getName());
        List<FileDto> fileDtosList = files.stream()
                .map(file -> modelMapper.map(file, FileDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(fileDtosList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileDto> getFileById(@PathVariable("id") Long id, Principal principal) {
        File file = fileService.getById(id);

        if (!file.getOwner().getNickName().equals(principal.getName())){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        FileDto fileDto = modelMapper.map(file, FileDto.class);
        return new ResponseEntity<>(fileDto, HttpStatus.OK);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("id") Long id, Principal principal) {
        File file = fileService.getById(id);

        if (!file.getOwner().getNickName().equals(principal.getName())){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        byte[] content = fileService.downloadById(id);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("content-disposition", "attachment; filename=" + file.getFileName());

        return new ResponseEntity<>(content, httpHeaders, HttpStatus.OK);
    }

    @PostMapping(value = "/upload",
            consumes = {"multipart/form-data"})
    public ResponseEntity<FileDto> uploadFile(
            @RequestPart("file") MultipartFile multipartFile, Principal principal) {
        User user = userService.getByNickName(principal.getName());
        File file = new File(
                multipartFile.getOriginalFilename(),
                user,
                FileStatus.ACTIVE,
                new Date(),
                new Date()
        );

        file.setUpdateDate(file.getUploadDate());

        File fileWithId = fileService.save(file, multipartFile);

        return new ResponseEntity<>(modelMapper.map(fileWithId, FileDto.class), HttpStatus.ACCEPTED);
    }

    @PutMapping
    public ResponseEntity<FileDto> updateFile(@RequestBody @Valid FileUpdateDto fileUpdateDto, Principal principal) {
        File file = fileService.getById(fileUpdateDto.getId());

        if (!file.getOwner().getNickName().equals(principal.getName())){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        file = new File();
        file.setId(fileUpdateDto.getId());
        file.setFileName(fileUpdateDto.getFileName());

        File updatedFile = fileService.update(file);

        return new ResponseEntity<>(
                modelMapper.map(updatedFile, FileDto.class),
                HttpStatus.ACCEPTED
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<FileDto> deleteById(@PathVariable("id") Long id, Principal principal) {
        File file = fileService.getById(id);

        if (!file.getOwner().getNickName().equals(principal.getName())){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        fileService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}