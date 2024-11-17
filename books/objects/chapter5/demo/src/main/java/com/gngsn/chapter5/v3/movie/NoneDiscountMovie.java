package com.gngsn.chapter5.v3.movie;

import com.gngsn.chapter5.v3.Money;
import com.gngsn.chapter5.v3.discount.DiscountCondition;

import java.time.Duration;

/**
 * v3. 비율 할인 정책과 관련된 영화
 */
public class NoneDiscountMovie extends Movie {

    public NoneDiscountMovie(String title, Duration runningTime, Money fee,
                             double percent, DiscountCondition... discountConditions) {
        super(title, runningTime, fee, discountConditions);
    }

    @Override
    protected Money calculateDiscountAmount() {
        return Money.ZERO;
    }
}
