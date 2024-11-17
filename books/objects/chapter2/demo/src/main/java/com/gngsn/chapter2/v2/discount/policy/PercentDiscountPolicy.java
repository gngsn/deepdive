package com.gngsn.chapter2.v2.discount.policy;

import com.gngsn.chapter2.v2.Screening;
import com.gngsn.chapter2.v2.Money;
import com.gngsn.chapter2.v2.discount.condition.DiscountCondition;

/**
 * Version1. 비율 할인 정책
 */
public class PercentDiscountPolicy extends DefaultDiscountPolicy {
    private double percent;

    public PercentDiscountPolicy(double percent, DiscountCondition... conditions) {
        super(conditions);
        this.percent = percent;
    }

    @Override
    protected Money getDiscountAmount(Screening screening) {
        return screening.getMovieFee().times(percent);
    }
}
