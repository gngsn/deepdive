package com.gngsn.chapter5.v3.movie;

import com.gngsn.chapter5.v3.Money;
import com.gngsn.chapter5.v3.discount.DiscountCondition;

import java.time.Duration;

/**
 * v3. 금액 할인 정책과 관련된 영화
 */
public class AmountDiscountMovie extends Movie {
    private Money discountAmount;

    public AmountDiscountMovie(String title, Duration runningTime, Money fee,
                               Money discountAmount, DiscountCondition... discountConditions) {
        super(title, runningTime, fee, discountConditions);
        this.discountAmount = discountAmount;
    }

    @Override
    protected Money calculateDiscountAmount() {
        return discountAmount;
    }
}
