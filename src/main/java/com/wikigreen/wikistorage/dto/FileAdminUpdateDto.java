package com.wikigreen.wikistorage.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class FileAdminUpdateDto extends BaseEntityDto{
    private String fileName;
    private Long userId;
}
