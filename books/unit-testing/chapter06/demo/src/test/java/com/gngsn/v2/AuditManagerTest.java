//package com.gngsn.v2;
//
//import org.junit.jupiter.api.Test;
//
//import java.util.Arrays;
//
//import org.mockito.Mockito.*;
//
//public class AuditManagerTest {
//
//    @Test
//    public void aNewFileIsCreatedWhenTheCurrentFileOverflows() {
//        IFileSystem fileSystemMock = mock(IFileSystem.class);
//        fileSystemMock.getFiles("audits")).thenReturn(new String[] {
//                "audits/audit_1.txt",
//                "audits/audit_2.txt"
//        });
//
//        when(fileSystemMock.readAllLines("audits/audit_2.txt")).thenReturn(Arrays.asList(
//                "Peter;2019-04-06T16:30:00",
//                "Jane;2019-04-06T16:40:00",
//                "Jack;2019-04-06T17:00:00"
//        ));
//
//        AuditManager sut = new AuditManager(3, "audits", fileSystemMock);
//
//        sut.addRecord("Alice", LocalDateTime.parse("2019-04-06T18:00:00"));
//
//        verify(fileSystemMock).writeAllText(
//                "audits/audit_3.txt",
//                "Alice;2019-04-06T18:00:00");
//    }
//}
