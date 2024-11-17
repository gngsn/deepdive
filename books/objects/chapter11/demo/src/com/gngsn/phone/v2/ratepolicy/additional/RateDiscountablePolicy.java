package com.gngsn.phone.v2.ratepolicy.additional;

import com.gngsn.phone.Money;
import com.gngsn.phone.v2.ratepolicy.RatePolicy;

/**
 * 기본 요금 할인 정책
 */
public class RateDiscountablePolicy extends AdditionalRatePolicy {
    private Money discountAmount;

    public RateDiscountablePolicy(Money discountAmount, RatePolicy next) {
        super(next);
        this.discountAmount = discountAmount;
    }

    @Override
    protected Money afterCalculated(Money fee) {
        return fee.minus(discountAmount);
    }
}