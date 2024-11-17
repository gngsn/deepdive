package com.gngsn.chapter5.v2.discount;

import com.gngsn.chapter5.v2.Screening;

/**
 * v2. 영화 할인 조건 인터페이스
 */
public interface DiscountCondition {

    boolean isSatisfiedBy(Screening screening);
}