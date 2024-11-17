package com.gngsn.chapter2.v1.discount.policy;

import com.gngsn.chapter2.v1.Money;
import com.gngsn.chapter2.v1.Screening;
import com.gngsn.chapter2.v1.discount.condition.DiscountCondition;

/**
 * Version1. 금액 할인 정책
 */
public class AmountDiscountPolicy extends DiscountPolicy {
    private Money discountAmount;

    public AmountDiscountPolicy(Money discountAmount, DiscountCondition... conditions) {
        super(conditions);
        this.discountAmount = discountAmount;
    }

    @Override
    protected Money getDiscountAmount(Screening screening) {
        return discountAmount;
    }
}
