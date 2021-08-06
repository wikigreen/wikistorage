package com.wikigreen.wikistorage.rest;

import com.wikigreen.wikistorage.dto.ResetPasswordDto;
import com.wikigreen.wikistorage.dto.UserDto;
import com.wikigreen.wikistorage.dto.UserProfileDto;
import com.wikigreen.wikistorage.dto.UserProfileUpdateDto;
import com.wikigreen.wikistorage.model.User;
import com.wikigreen.wikistorage.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("api/v1/profile")
public class UserRestControllerV1 {
    private UserService userService;
    private ModelMapper modelMapper;

    @Autowired
    public UserRestControllerV1(UserService userService, ModelMapper modelMapper){
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserProfileDto> getUser(Principal principal){
        User user = userService.getByNickName(principal.getName());

        UserDto userDto = modelMapper.map(user, UserDto.class);

        UserProfileDto userProfileDto = modelMapper.map(userDto, UserProfileDto.class);

        return new ResponseEntity<>(userProfileDto, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<UserProfileDto> update(@RequestBody @Valid UserProfileUpdateDto userUpdateDto,
                                                 Principal principal){
        User user = modelMapper.map(userUpdateDto, User.class);
        user.setId(userService.getByNickName(principal.getName()).getId());
        User updatedUser = userService.update(user);
        UserDto userDto = modelMapper.map(updatedUser, UserDto.class);
        UserProfileDto updatedUserDto = modelMapper.map(userDto, UserProfileDto.class);

        return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
    }

    @PutMapping("/reset_password")
    public ResponseEntity<UserProfileDto> resetPassword(@RequestBody @Valid ResetPasswordDto passwordDto,
                                                        Principal principal){
        userService.changePassword(principal.getName(), passwordDto.getOldPassword(), passwordDto.getNewPassword());
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping
    public ResponseEntity<UserProfileDto> deleteUser(Principal principal){
        User user = userService.getByNickName(principal.getName());
        userService.deleteById(user.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
