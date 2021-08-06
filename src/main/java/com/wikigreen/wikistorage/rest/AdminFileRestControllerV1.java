package com.wikigreen.wikistorage.rest;

import com.wikigreen.wikistorage.dto.FileDto;
import com.wikigreen.wikistorage.dto.FileAdminUpdateDto;
import com.wikigreen.wikistorage.model.File;
import com.wikigreen.wikistorage.model.FileStatus;
import com.wikigreen.wikistorage.model.User;
import com.wikigreen.wikistorage.service.FileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping({"api/v1/admin/file",
                "api/v1/moderator/file"})
public class AdminFileRestControllerV1 {
    private FileService fileService;
    private ModelMapper modelMapper;

    @Autowired
    public AdminFileRestControllerV1(FileService fileService, ModelMapper modelMapper) {
        this.fileService = fileService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<FileDto>> getAllFiles() {
        List<File> files = fileService.getAll();
        List<FileDto> fileDtosList = files.stream()
                .map(file -> modelMapper.map(file, FileDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(fileDtosList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileDto> getFileDataById(@PathVariable("id") Long id) {
        File file = fileService.getById(id);
        FileDto fileDto = modelMapper.map(file, FileDto.class);
        return new ResponseEntity<>(fileDto, HttpStatus.OK);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("id") Long id) {
        File file = fileService.getById(id);
        byte[] content = fileService.downloadById(id);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("content-disposition", "attachment; filename=" + file.getFileName());

        return new ResponseEntity<>(content, httpHeaders, HttpStatus.OK);
    }

    @PostMapping(value = "/upload",
            consumes = {"multipart/form-data"})
    public ResponseEntity<FileDto> uploadFile(
            @RequestParam("user_id") Long userId,
            @RequestPart("file") MultipartFile multipartFile) {
        User user = new User();
        user.setId(userId);

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
    public ResponseEntity<FileDto> updateFile(@RequestBody @Valid FileAdminUpdateDto fileAdminUpdateDto) {
        File file = new File();
        file.setId(fileAdminUpdateDto.getId());
        file.setFileName(fileAdminUpdateDto.getFileName());

        User user = new User();
        user.setId(fileAdminUpdateDto.getUserId());
        file.setOwner(user);

        File updatedFile = fileService.update(file);

        return new ResponseEntity<>(
                modelMapper.map(updatedFile, FileDto.class),
                HttpStatus.ACCEPTED
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<FileDto> deleteById(@PathVariable("id") Long id) {
        fileService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
