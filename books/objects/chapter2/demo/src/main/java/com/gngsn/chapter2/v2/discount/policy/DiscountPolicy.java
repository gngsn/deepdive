package com.gngsn.chapter2.v2.discount.policy;

import com.gngsn.chapter2.v2.Money;
import com.gngsn.chapter2.v2.Screening;

/**
 * Version2. 할인 정책 인터페이스
 * - NoneDiscountPolicy을 위해 작성된 인터페이스
 */
public interface DiscountPolicy {

    Money calculateDiscountAmount(Screening screening);
}
