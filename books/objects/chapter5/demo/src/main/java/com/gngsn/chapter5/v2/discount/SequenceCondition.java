package com.gngsn.chapter5.v2.discount;

import com.gngsn.chapter5.v2.Screening;

/**
 * v2. DiscountCondition 분리 - SequenceCondition
 */
public class SequenceCondition implements DiscountCondition {
    private int sequence;

    public boolean isSatisfiedBy(Screening screening) {
        return sequence == screening.getSequence();
    }
}