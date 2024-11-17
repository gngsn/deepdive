package com.gngsn.phone.v2.ratepolicy.additional;

import com.gngsn.phone.Money;
import com.gngsn.phone.v2.Phone;
import com.gngsn.phone.v2.ratepolicy.RatePolicy;

/**
 * 부가 정책은 RatePolicy 인터페이스를 구현해야 하며, 내부에 또 다른 RatePolicy 인스턴스를 합성할 수 있어야 함
 */
public abstract class AdditionalRatePolicy implements RatePolicy {

    private RatePolicy next;

    public AdditionalRatePolicy(RatePolicy next) {
        this.next = next;
    }

    @Override
    public Money calculateFee(Phone phone) {
        Money fee = next.calculateFee(phone);
        return afterCalculated(fee);
    }

    abstract protected Money afterCalculated(Money fee);
}
