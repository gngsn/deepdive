package com.gngsn.chapter5.v3.movie;

import com.gngsn.chapter5.v3.Money;
import com.gngsn.chapter5.v3.discount.DiscountCondition;

import java.time.Duration;

/**
 * v3. 비율 할인 정책과 관련된 영화
 */
public class PercentDiscountMovie extends Movie {
    private double percent;

    public PercentDiscountMovie(String title, Duration runningTime, Money fee,
                                double percent, DiscountCondition... discountConditions) {
        super(title, runningTime, fee, discountConditions);
        this.percent = percent;
    }

    @Override
    protected Money calculateDiscountAmount() {
        return getFee().times(percent);
    }
}
