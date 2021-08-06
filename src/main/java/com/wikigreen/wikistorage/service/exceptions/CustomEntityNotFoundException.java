package com.wikigreen.wikistorage.service.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CustomEntityNotFoundException extends RuntimeException {
    private final String propertyValue;
    private final String entityName;
    private final String propertyName;

    public String getFormattedMessage(){
        return String.format("%s with %s %s is not found", entityName, propertyName, propertyValue);
    }

}
