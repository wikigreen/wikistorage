package com.wikigreen.wikistorage.service.exceptions;

public class NotUniqueDataException extends RuntimeException {
    public NotUniqueDataException(){
        super();
    }

    public NotUniqueDataException(String message){
        super(message);
    }
}
