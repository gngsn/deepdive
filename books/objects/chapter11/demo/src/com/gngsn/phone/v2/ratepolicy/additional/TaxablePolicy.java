package com.gngsn.phone.v2.ratepolicy.additional;

import com.gngsn.phone.Money;
import com.gngsn.phone.v2.ratepolicy.RatePolicy;

/**
 * 세금 정책
 */
public class TaxablePolicy extends AdditionalRatePolicy {
    private double taxRatio;

    public TaxablePolicy(double taxRatio, RatePolicy next) {
        super(next);
        this.taxRatio = taxRatio;
    }

    @Override
    protected Money afterCalculated(Money fee) {
        return fee.plus(fee.times(taxRatio));
    }
}