package com.gngsn.chapter2.v1.discount.policy;

import com.gngsn.chapter2.v1.Money;
import com.gngsn.chapter2.v1.Screening;


/**
 * Version1. 할인 정책이 없을 경우의 할인 정책
 * - 예외 케이스를 최소화하고 일관성을 유지하기 위해 분기처리를 하지 않고 NoneDiscountPolicy를 생성한다.
 */
public class NoneDiscountPolicy extends DiscountPolicy {

    @Override
    protected Money getDiscountAmount(Screening screening) {
        return Money.ZERO;
    }
}
