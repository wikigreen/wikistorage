package com.wikigreen.wikistorage.model;

public enum  FileStatus {
    ACTIVE("ACTIVE"), BANNED("BANNED"), DELETED("DELETED");

    private final String statusName;

    FileStatus(String statusName) {
        this.statusName = statusName;
    }

    @Override
    public String toString(){
        return this.statusName;
    }
}
