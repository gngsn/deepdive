package com.gngsn.v3;


public class FileUpdate {
    public final String fileName;
    public final String newContent;

    public FileUpdate(String fileName, String newContent){
        this.fileName = fileName;
        this.newContent = newContent;
    }
}
