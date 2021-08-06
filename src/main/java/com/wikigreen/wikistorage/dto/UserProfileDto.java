package com.wikigreen.wikistorage.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class UserProfileDto extends BaseEntityDto{
    private String firstName;
    private String lastName;
    private String email;
    private String nickName;
    @JsonManagedReference
    @JsonProperty("files")
    @JsonIgnoreProperties("owner")
    private List<FileDto> files;
    @JsonManagedReference
    @JsonProperty("events")
    @JsonIgnoreProperties("owner")
    private List<EventDto> events;
}
