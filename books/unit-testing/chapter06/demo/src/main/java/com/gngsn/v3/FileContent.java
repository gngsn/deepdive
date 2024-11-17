package com.gngsn.v3;

import java.nio.file.Files;
import java.util.List;

public class FileContent {
    public final String fileName;
    public final String[] lines;

    public FileContent(String fileName, String[] lines) {
        this.fileName = fileName;
        this.lines = lines;
    }
}