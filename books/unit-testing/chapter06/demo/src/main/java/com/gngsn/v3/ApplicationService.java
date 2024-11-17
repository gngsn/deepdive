package com.gngsn.v3;


import java.time.LocalDateTime;

public class ApplicationService {
    private final String _directoryName;
    private final AuditManager _auditManager;
    private final Persister _persister;

    public ApplicationService(String directoryName, int maxEntriesPerFile) {
        _directoryName = directoryName;
        _auditManager = new AuditManager(maxEntriesPerFile);
        _persister = new Persister();
    }

    public void AddRecord(String visitorName, LocalDateTime timeOfVisit) {
        FileContent[] files = _persister.ReadDirectory(_directoryName);
        FileUpdate update = _auditManager.AddRecord(files, visitorName, timeOfVisit);
        _persister.ApplyUpdate(_directoryName, update);
    }
}