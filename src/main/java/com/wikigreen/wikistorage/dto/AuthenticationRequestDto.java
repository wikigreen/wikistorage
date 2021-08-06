package com.wikigreen.wikistorage.dto;

import lombok.Data;

@Data
public class AuthenticationRequestDto {
    private String nickName;
    private String password;
}
