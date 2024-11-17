package com.gngsn;

import java.util.ArrayList;
import java.util.List;

public abstract class Phone {

    private List<Call> calls = new ArrayList<>();

    public Money calculateFee() {
        Money result = Money.ZERO;

        for (Call call : calls) {
            result = result.plus(calculateCallFee(call));
        }

        return result;

    }

    abstract protected Money calculateCallFee(Call call);

    /**
     * 자식 클래스에게 전체 요금을 계산한 후에 수행할 로직을 추가할 수 있는 기회를 제공
     * - super 호출보다 결합도를 낮추는 방식
     */
    protected abstract Money afterCalculated(Money fee);

    public List<Call> getCalls() {
        return calls;
    }
}

