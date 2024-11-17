package com.gngsn.v1;

import com.gngsn.Call;
import com.gngsn.Money;
import com.gngsn.Phone;
import com.gngsn.RatePolicy;

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
