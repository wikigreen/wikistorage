package com.wikigreen.wikistorage.rest;

import com.wikigreen.wikistorage.dto.*;
import com.wikigreen.wikistorage.model.Event;
import com.wikigreen.wikistorage.model.File;
import com.wikigreen.wikistorage.model.User;
import com.wikigreen.wikistorage.service.EventService;
import com.wikigreen.wikistorage.service.FileService;
import com.wikigreen.wikistorage.service.UserService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/users")
public class AdminUserRestControllerV1 {
    private UserService userService;
    private FileService fileService;
    private EventService eventService;
    private ModelMapper modelMapper;

    @Autowired
    public AdminUserRestControllerV1(UserService userService,
                                     FileService fileService,
                                     EventService eventService,
                                     ModelMapper modelMapper)
    {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.fileService = fileService;
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> userDtoList = userService.getAll().stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        return new ResponseEntity<>(modelMapper.map(user, UserDto.class), HttpStatus.OK);
    }

    @GetMapping("/{id}/files")
    public ResponseEntity<List<FileDto>> getUsersFiles(@PathVariable("id") Long id) {
        List<File> files = fileService.getByUserId(id);
        List<FileDto> fileDtos = files.stream()
                .map(file -> modelMapper.map(file, FileDto.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(fileDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}/events")
    public ResponseEntity<List<EventDto>> getUsersEvents(@PathVariable("id") Long id) {
        List<Event> events = eventService.getByUserId(id);
        List<EventDto> eventDtos = events.stream()
                .map(event -> modelMapper.map(event, EventDto.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(eventDtos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        User user = modelMapper.map(userCreateDto, User.class);
        user.setEvents(new ArrayList<>());
        user.setFiles(new ArrayList<>());

        User userWithId = userService.save(user);

        UserDto userDto = modelMapper.map(userWithId, UserDto.class);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@RequestBody @Valid UserUpdateDto userUpdateDto) {
        User user = modelMapper.map(userUpdateDto, User.class);

        User newUser = userService.update(user);

        UserDto newUserDto = modelMapper.map(newUser, UserDto.class);

        return new ResponseEntity<>(newUserDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}
