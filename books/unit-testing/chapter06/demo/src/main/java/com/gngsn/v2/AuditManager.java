package com.gngsn.v2;

import com.gngsn.File;
import com.gngsn.Path;
import kotlin.Pair;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

public class AuditManager {
    private final int _maxEntriesPerFile;
    private final String _directoryName;
    private final IFileSystem _fileSystem;

    public AuditManager(
            int maxEntriesPerFile,
            String directoryName,
            IFileSystem fileSystem) {
        _maxEntriesPerFile = maxEntriesPerFile;
        _directoryName = directoryName;
        _fileSystem = fileSystem;
    }

    public void AddRecord(String visitorName, LocalDateTime timeOfVisit)
    {
        List<String> filePaths = _fileSystem                               // 1
                .getFiles(_directoryName);                                 // 1
        Pair<Integer, String>[] sorted = sortByIndex(filePaths);

        String newRecord = visitorName + ';' + timeOfVisit;

        if (sorted.length == 0) {
            String newFile = Path.combine(_directoryName, "audit_1.txt");
            _fileSystem.writeAllText(newFile, newRecord);              // 1
            return;
        }

        Pair<Integer, String> currentFile = sorted[sorted.length-1];
        List<String> lines = _fileSystem                                // 1
            .readAllLines(currentFile.getSecond());                     // 1

        if (lines.size() < _maxEntriesPerFile) {
            lines.add(newRecord);
            String newContent = String.join("\r\n", lines);
            _fileSystem.writeAllText(                                    // 1
                    currentFile.getSecond(), newContent);                // 1
        } else {
            int newIndex = currentFile.getFirst() + 1;
            String newName = "audit_%s.txt".formatted(newIndex);
            String newFile = Path.combine(_directoryName, newName);
            File.writeAllText(newFile, newRecord);                       // 1
        }
    }

    private Pair<Integer, String>[] sortByIndex(List<String> filePaths) {
        return IntStream.range(0, filePaths.size())
                .mapToObj(i -> new Pair<>(i, filePaths.get(i)))
                .toArray(Pair[]::new);
    }
}