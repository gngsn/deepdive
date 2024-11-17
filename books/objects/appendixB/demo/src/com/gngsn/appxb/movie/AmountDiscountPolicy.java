package com.gngsn.appxb.movie;


import com.gngsn.chapter2.v2.Money;
import com.gngsn.chapter2.v2.Screening;
import com.gngsn.chapter2.v2.discount.condition.DiscountCondition;

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