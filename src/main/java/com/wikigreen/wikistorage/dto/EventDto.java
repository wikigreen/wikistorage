package com.wikigreen.wikistorage.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wikigreen.wikistorage.model.EventType;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class EventDto extends BaseEntityDto{
    private EventType eventType;
    private Date date;
    @JsonIgnoreProperties("owner")
    private FileDto file;
}
