package com.wikigreen.wikistorage.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class FileUpdateDto extends BaseEntityDto{
    private String fileName;
}
