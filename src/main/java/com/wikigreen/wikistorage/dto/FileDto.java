package com.wikigreen.wikistorage.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wikigreen.wikistorage.model.FileStatus;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class FileDto extends BaseEntityDto{
    private String fileName;
    @JsonProperty("owner")
    @JsonIgnoreProperties({"files", "events"})
    private UserDto owner;
    private FileStatus status;
    private Date uploadDate;
    private Date updateDate;
}
