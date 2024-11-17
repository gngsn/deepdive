package com.gngsn.v2;

import com.gngsn.DateTimeInterval;
import com.gngsn.Money;

import java.time.Duration;

/**
 * 단위 시간당 요금
 */
public class FeePerDuration {

    private Money fee;
    private Duration duration;

    public FeePerDuration(Money fee, Duration duration) {
        this.fee = fee;
        this.duration = duration;
    }

    /**
     * 일정 기간 동안의 요금을 계산
     */
    public Money calculate(DateTimeInterval interval) {
        return fee.times(Math.ceil((double)interval.duration().toNanos() / duration.toNanos()));
    }
}
