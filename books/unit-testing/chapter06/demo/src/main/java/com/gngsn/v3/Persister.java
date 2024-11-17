package com.gngsn.v3;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Persister {
    public FileContent[] ReadDirectory(String directoryName) {
            return Arrays.stream(Directory.getFiles(directoryName))
                .map(x -> getFileContentList(x))
                    .toArray(FileContent[]::new);
    }

    @NotNull
    private static FileContent getFileContentList(Path x) {
        try {
            return new FileContent(x.getFileName().toString(), Files.readAllLines(x).toArray(String[]::new));
        } catch (IOException e) {
            throw new RuntimeException("Invalid File path");
        }
    }

    public void ApplyUpdate(String directoryName, FileUpdate update) {
    }

    private class Directory {
        static Path[] getFiles(String directoryName) {
            try {
                return Files.walk(Path.of(directoryName)).toArray(Path[]::new);
            } catch (Exception e) {
                throw new RuntimeException("Invalid File path");
            }
        }
    }
}