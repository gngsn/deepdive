package com.gngsn.chapter2.v2.discount.policy;

import com.gngsn.chapter2.v2.Money;
import com.gngsn.chapter2.v2.Screening;
import com.gngsn.chapter2.v2.discount.condition.DiscountCondition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Version2. 할인 정책 추상 클래스
 * - DiscountPolicy을 구현한 클래스로 변경
 */
public abstract class DefaultDiscountPolicy implements DiscountPolicy {
    private List<DiscountCondition> conditions = new ArrayList<>();

    public DefaultDiscountPolicy(DiscountCondition...conditions) {
        this.conditions = Arrays.asList(conditions);
    }

    @Override
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
