package com.gngsn.v1.policy;

import com.gngsn.phone.Call;
import com.gngsn.phone.Money;
import com.gngsn.phone.v2.ratepolicy.BasicRatePolicy;

import java.time.Duration;

/**
 *
 */
public class FixedFeePolicy extends BasicRatePolicy {

    private Money amount;

    private Duration seconds;

    public FixedFeePolicy(Money amount, Duration seconds) {
        this.amount = amount;
        this.seconds = seconds;
    }

    @Override

    protected Money calculateCallFee(Call call) {
        return amount.times(call.getDuration().getSeconds() / seconds.getSeconds());
    }

}