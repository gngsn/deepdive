package com.gngsn.chapter2.v1.discount.condition;

import com.gngsn.chapter2.v1.Screening;

public interface DiscountCondition {

    boolean isSatisfiedBy(Screening screening);
}
