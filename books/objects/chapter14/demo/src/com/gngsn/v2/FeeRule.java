package com.gngsn.v2;

import com.gngsn.Call;
import com.gngsn.Money;
import com.gngsn.v2.condition.FeeCondition;

public class FeeRule {
    private FeeCondition feeCondition;
    private FeePerDuration feePerDuration;

    public FeeRule(FeeCondition feeCondition, FeePerDuration feePerDuration) {
        this.feeCondition = feeCondition;
        this.feePerDuration = feePerDuration;
    }

    /**
     * FeeCondition 에게 findTimeIntervals 메시지를 전송해서
     * 조건을 만족하는 시간의 목록을 반환받은 후 feePerDuration의 값을 이용해 요금을 계산
     */
    public Money calculateFee(Call call) {
        return feeCondition.findTimeIntervals(call)
                .stream()
                .map(each -> feePerDuration.calculate(each))
                .reduce(Money.ZERO, (first, second) -> first.plus(second));
    }
}
