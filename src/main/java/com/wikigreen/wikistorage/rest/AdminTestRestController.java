package com.wikigreen.wikistorage.rest;

import com.wikigreen.wikistorage.dto.RoleDto;
import com.wikigreen.wikistorage.model.Role;
import com.wikigreen.wikistorage.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/test")
public class AdminTestRestController {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<RoleDto>> getRoles(){
        List<Role> roles = roleRepository.findByRoleNameIn(
                List.of("ADMIN", "MODERATOR", "USER"));
        List<RoleDto> roleDtos = roles
                .stream()
                .map(role -> modelMapper.map(role, RoleDto.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(roleDtos, HttpStatus.OK);
    }
}
