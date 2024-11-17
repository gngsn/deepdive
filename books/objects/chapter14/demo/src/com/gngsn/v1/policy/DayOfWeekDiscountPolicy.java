package com.gngsn.v1.policy;

import com.gngsn.Call;
import com.gngsn.DateTimeInterval;
import com.gngsn.Money;
import com.gngsn.v1.BasicRatePolicy;
import com.gngsn.v1.rule.DayOfWeekDiscountRule;

import java.util.ArrayList;
import java.util.List;

public class DayOfWeekDiscountPolicy extends BasicRatePolicy {

    private List<DayOfWeekDiscountRule> rules = new ArrayList<>();

    public DayOfWeekDiscountPolicy(List<DayOfWeekDiscountRule> rules) {
        this.rules = rules;
    }

    @Override
    protected Money calculateCallFee(Call call) {
        Money result = Money.ZERO;
        for(DateTimeInterval interval : call.getInterval().splitByDay()) {
            for(DayOfWeekDiscountRule rule: rules) {
                result.plus(rule.calculate(interval));
            }
        }
        return result;
    }
}