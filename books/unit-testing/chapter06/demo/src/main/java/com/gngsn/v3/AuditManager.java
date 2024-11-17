package com.gngsn.v3;

import kotlin.Pair;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

public class AuditManager {
    private final int _maxEntriesPerFile;

    public AuditManager(int maxEntriesPerFile) {
        _maxEntriesPerFile = maxEntriesPerFile;
    }

    public FileUpdate AddRecord(FileContent[] files, String visitorName, LocalDateTime timeOfVisit) {
        Pair<Integer, FileContent>[] sorted = sortByIndex(files);

        String newRecord = visitorName + ';' + timeOfVisit;

        if (sorted.length == 0) {
            return new FileUpdate("audit_1.txt", newRecord);
        }

        Pair<Integer, FileContent> currentFile = sorted[sorted.length-1];
        String[] lines = currentFile.getSecond().lines;

        if (lines.length < _maxEntriesPerFile) {
            lines[lines.length-1] = newRecord;
            String newContent = String.join("\r\n", lines);
            return new FileUpdate(currentFile.getSecond().fileName, newContent);
        } else {
            int newIndex = currentFile.getFirst() + 1;
            String newName = "audit_%s.txt".formatted(newIndex);
            return new FileUpdate(newName, newRecord);
        }
    }

    private Pair<Integer, FileContent>[] sortByIndex(FileContent[] files) {
        return IntStream.range(0, files.length)
                .mapToObj(i -> new Pair<>(i, files[i]))
                .toArray(Pair[]::new);
    }
}