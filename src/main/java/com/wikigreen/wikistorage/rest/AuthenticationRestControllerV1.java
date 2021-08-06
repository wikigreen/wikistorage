package com.wikigreen.wikistorage.rest;

import com.wikigreen.wikistorage.dto.AuthenticationRequestDto;
import com.wikigreen.wikistorage.dto.LoginResponseDto;
import com.wikigreen.wikistorage.dto.UserProfileDto;
import com.wikigreen.wikistorage.dto.UserRegisterDto;
import com.wikigreen.wikistorage.model.User;
import com.wikigreen.wikistorage.security.jwt.JwtTokenProvider;
import com.wikigreen.wikistorage.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationRestControllerV1 {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthenticationRestControllerV1(
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            UserService userService,
            ModelMapper modelMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody AuthenticationRequestDto requestDto){
        String nickName = requestDto.getNickName();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(nickName, requestDto.getPassword()));

        User user = userService.getByNickName(nickName);

        String token = jwtTokenProvider.createToken(user);

        LoginResponseDto responseDto = new LoginResponseDto(nickName, token);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/registration")
    public ResponseEntity<UserProfileDto> registration(@RequestBody @Valid UserRegisterDto userDto){
        User user = modelMapper.map(userDto, User.class);

        userService.save(user);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
