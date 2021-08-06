package com.wikigreen.wikistorage.model;

public enum UserStatus {
    ACTIVE("ACTIVE"), BANNED("BANNED"), DELETED("DELETED");

    private final String statusName;

    UserStatus(String statusName){
        this.statusName = statusName;
    }

    @Override
    public String toString(){
        return statusName;
    }
}
