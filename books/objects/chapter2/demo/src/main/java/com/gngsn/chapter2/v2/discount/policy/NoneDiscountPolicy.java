package com.gngsn.chapter2.v2.discount.policy;

import com.gngsn.chapter2.v2.Money;
import com.gngsn.chapter2.v2.Screening;


/**
 * Version2. 할인 정책이 없을 경우의 할인 정책
 * - 예외 케이스를 최소화하고 일관성을 유지하기 위해 분기처리를 하지 않고 NoneDiscountPolicy를 생성한다.
 * - DiscountPolicy interface를 구현한다.
 */
public class NoneDiscountPolicy implements DiscountPolicy {
    @Override
    public Money calculateDiscountAmount(Screening screening) {
        return Money.ZERO;
    }
}
