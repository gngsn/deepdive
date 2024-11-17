package com.gngsn.phone.v2;


import com.gngsn.phone.Call;
import com.gngsn.phone.Money;
import com.gngsn.phone.v2.ratepolicy.RatePolicy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Phone {

    private RatePolicy ratePolicy;  // 추가 -> 합성
    private List<Call> calls = new ArrayList<>();

    public Phone(RatePolicy ratePolicy) {
        this.ratePolicy = ratePolicy;
    }

    public List<Call> getCalls() {
        return Collections.unmodifiableList(calls);
    }

    public Money calculateFee() {
        return ratePolicy.calculateFee(this);
    }
}

