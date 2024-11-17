package com.gngsn.chapter5.v3.discount;

import com.gngsn.chapter5.v3.Screening;

/**
 * v2. 영화 할인 조건 인터페이스
 */
public interface DiscountCondition {

    boolean isSatisfiedBy(Screening screening);
}