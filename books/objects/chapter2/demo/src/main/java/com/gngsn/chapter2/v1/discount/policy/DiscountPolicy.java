package com.gngsn.chapter2.v1.discount.policy;

import com.gngsn.chapter2.v1.discount.condition.DiscountCondition;
import com.gngsn.chapter2.v1.Money;
import com.gngsn.chapter2.v1.Screening;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Version1. 할인 정책 추상 클래스
 */
public abstract class DiscountPolicy {
    private List<DiscountCondition> conditions = new ArrayList<>();

    public DiscountPolicy(DiscountCondition ...conditions) {
        this.conditions = Arrays.asList(conditions);
    }

    public Money calculateDiscountAmount(Screening screening) {
        for(DiscountCondition each: conditions) {
            if (each.isSatisfiedBy(screening)) {
                return getDiscountAmount(screening);
            }
        }

        return Money.ZERO;
    }

    abstract protected Money getDiscountAmount(Screening screening);
}
