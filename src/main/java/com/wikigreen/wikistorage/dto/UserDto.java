package com.wikigreen.wikistorage.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wikigreen.wikistorage.model.UserStatus;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UserDto extends BaseEntityDto{
    @JsonManagedReference
    @JsonProperty("files")
    private List<FileDto> files;
    @JsonManagedReference
    @JsonProperty("events")
    private List<EventDto> events;
    private String firstName;
    private String lastName;
    private String email;
    private String nickName;
    private List<RoleDto> roles;
    private UserStatus userStatus;
}
