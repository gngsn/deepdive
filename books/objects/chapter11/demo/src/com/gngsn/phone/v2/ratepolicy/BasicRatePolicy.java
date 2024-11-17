package com.gngsn.phone.v2.ratepolicy;

import com.gngsn.phone.Call;
import com.gngsn.phone.Money;
import com.gngsn.phone.v2.Phone;

public abstract class BasicRatePolicy implements RatePolicy {

    @Override
    public Money calculateFee(Phone phone) {
        Money result = Money.ZERO;

        for(Call call : phone.getCalls()) {
            result.plus(calculateCallFee(call));
        }

        return result;
    }

    protected abstract Money calculateCallFee(Call call);
}
