package com.gngsn;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Directory {
    public static List<String> getFiles(String directoryName)  {
        System.out.println("Directory::getFiles");
        try {
            return Files.readAllLines(Path.of(directoryName));
        } catch (IOException ie) {
            System.out.println("Not Found File Error");
            throw new RuntimeException("File path is wrong.");
        }
    }
}
