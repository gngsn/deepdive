package com.gngsn;

import kotlin.Pair;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

/**
 * 파일 시스템과 밀접하게 연결
 * 테스트 전에 파일을 올바른 위치에 배치하고, 테스트가 끝나면 해당 파일을 읽고 내용을 확인한 후 삭제해야 함
 */
public class AuditManager {
    private int _maxEntriesPerFile;
    private String _directoryName;

    public AuditManager(int maxEntriesPerFile, String directoryName) {
        _maxEntriesPerFile = maxEntriesPerFile;
        _directoryName = directoryName;
    }

    public void addRecord(String visitorName, LocalDateTime timeOfVisit) {
        // 작업 디렉터리에서 전체 파일 목록 검색
        List<String> filePaths = Directory.getFiles(_directoryName);
        // 인덱스 별 정렬
        Pair<Integer, String>[] sorted = sortByIndex(filePaths);

        String newRecord = visitorName + ';' + timeOfVisit;

        // 감사 파일이 없으면 단일 레코드로 첫 번째 파일 생성
        if (sorted.length == 0) {
            String newFile = Path.combine(_directoryName, "audit_1.txt");
            File.writeAllText(newFile, newRecord);
            return;
        }

        Pair<Integer, String> currentFile = sorted[sorted.length-1];
        List<String> lines = File.readAllLines(currentFile.getSecond());

        // 감사 파일이 있으면 최신 파일을 가져와서, 파일의 항목 수가 한계에 도달했는지에 따라 새 레코드를 추가하거나 새 파일 생성
        if (lines.size() < _maxEntriesPerFile) {
            lines.add(newRecord);
            String newContent = String.join("\r\n", lines);
            File.writeAllText(currentFile.getSecond(), newContent);
        } else {
            int newIndex = currentFile.getFirst() + 1;
            String newName = "audit_%s.txt".formatted(newIndex);
            String newFile = Path.combine(_directoryName, newName);
            File.writeAllText(newFile, newRecord);
        }
    }

    private Pair<Integer, String>[] sortByIndex(List<String> filePaths) {
        return IntStream.range(0, filePaths.size())
                .mapToObj(i -> new Pair<>(i, filePaths.get(i)))
                .toArray(Pair[]::new);
    }
}