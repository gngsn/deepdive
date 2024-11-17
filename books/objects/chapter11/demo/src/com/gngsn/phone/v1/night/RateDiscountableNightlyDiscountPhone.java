package com.gngsn.phone.v1.night;

import com.gngsn.phone.Money;

import java.time.Duration;

public class RateDiscountableNightlyDiscountPhone extends com.gngsn.phone.v1.night.NightlyDiscountPhone {
    private Money discountAmount;

    public RateDiscountableNightlyDiscountPhone(Money nightlyAmount, Money regularAmount, Duration seconds, Money discountAmount) {
        super(nightlyAmount, regularAmount, seconds);
        this.discountAmount = discountAmount;
    }

    @Override
    protected Money afterCalculated(Money fee) {
        return fee.minus(discountAmount);
    }
}