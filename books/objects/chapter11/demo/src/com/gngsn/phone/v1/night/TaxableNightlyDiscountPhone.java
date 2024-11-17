package com.gngsn.phone.v1.night;

import com.gngsn.phone.Money;

import java.time.Duration;

public class TaxableNightlyDiscountPhone extends com.gngsn.phone.v1.night.NightlyDiscountPhone {
    private double taxRate;

    public TaxableNightlyDiscountPhone(Money nightlyAmount,
                                       Money regularAmount, Duration seconds, double taxRate) {
        super(nightlyAmount, regularAmount, seconds);
        this.taxRate = taxRate;
    }

    @Override
    protected Money afterCalculated(Money fee) {
        return fee.plus(fee.times(taxRate));
    }
}