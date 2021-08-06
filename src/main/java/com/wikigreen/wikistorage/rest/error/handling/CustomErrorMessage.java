package com.wikigreen.wikistorage.rest.error.handling;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class CustomErrorMessage {
    private int httpStatusCode;
    private List<String> messages;
}
