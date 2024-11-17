package com.gngsn.phone;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * v1. 개별 통화 기간을 저장하는 Call Class
 * - 통화 시작 시간(from)과 통화 종료 시간(to)을 인스턴스 변수로 포함
 */
public class Call {
    private LocalDateTime from;
    private LocalDateTime to;

    public Call(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    public Duration getDuration() {
        return Duration.between(from, to);
    }

    public LocalDateTime getFrom() {
        return from;
    }
}