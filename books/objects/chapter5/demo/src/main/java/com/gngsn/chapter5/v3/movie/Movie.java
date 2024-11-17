package com.gngsn.chapter5.v3.movie;

import com.gngsn.chapter5.v3.Money;
import com.gngsn.chapter5.v3.Screening;
import com.gngsn.chapter5.v3.discount.DiscountCondition;

import java.time.Duration;
import java.util.List;

/**
 * v3. 영화
 */
public abstract class Movie {
    private String title;
    private Duration runningTime;
    private Money fee;
    private List<DiscountCondition> discountConditions;

    public Movie(String title, Duration runningTime, Money fee,
                 DiscountCondition... discountConditions) {
        this.title = title;
        this.runningTime = runningTime;
        this.fee = fee;
        this.discountConditions = List.of(discountConditions);
    }

    public Money getFee() {
        return fee;
    }

    /**
     * Message: 가격을 계산하라
     * From: Screening Entity
     */
    public Money calculateMovieFee(Screening screening) {
        if (isDiscountable(screening)) {
            return fee.minus(calculateDiscountAmount());
        }

        return fee;
    }

    private boolean isDiscountable(Screening screening) {
        return discountConditions.stream()
                .anyMatch(condition -> condition.isSatisfiedBy(screening));
    }

    abstract protected Money calculateDiscountAmount();
}
