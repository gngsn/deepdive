package com.gngsn.chapter2.v2.discount.condition;

import com.gngsn.chapter2.v2.Screening;

public interface DiscountCondition {

    boolean isSatisfiedBy(Screening screening);
}
