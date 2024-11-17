package com.gngsn.phone.v3;

import com.gngsn.phone.Money;
import com.gngsn.phone.v2.Phone;
import com.gngsn.phone.v2.ratepolicy.NightlyDiscountPolicy;
import com.gngsn.phone.v2.ratepolicy.RegularPolicy;
import com.gngsn.phone.v2.ratepolicy.additional.RateDiscountablePolicy;
import com.gngsn.phone.v2.ratepolicy.additional.TaxablePolicy;

import java.time.Duration;

public class RatePolicyAgent {
    public static void main(String[] args) {
        // 일반 요금제 + 세금 정책
        Phone phone = new Phone(
                new TaxablePolicy(0.05, new RegularPolicy(Money.wons(10), Duration.ofSeconds(5))));

        // ( 일반 요금제 + 기본 요금 할인 정책 ) + 세금 정책
        Phone phone2 = new Phone(
                new TaxablePolicy(0.05, new RateDiscountablePolicy(Money.wons(1000),
                        new RegularPolicy(Money.wons(10), Duration.ofSeconds(5)))));


        // 일반 요금제 + 기본 요금 할인 정책 + 세금 정책, 세금 정책과 기본 요금 할인 정책이 적용되는 순서 변경
        Phone phone3 = new Phone(
                new RateDiscountablePolicy(Money.wons(1000),
                        new TaxablePolicy(0.05, new RegularPolicy(Money.wons(10), Duration.ofSeconds(5)))));

        // 심야 할인 요금제
        Phone phone4 = new Phone(
                new RateDiscountablePolicy(Money.wons(1000),
                        new TaxablePolicy(0.05,
                                new NightlyDiscountPolicy(Money.wons(15), Money.wons(10), Duration.ofSeconds(5)))));
    }
}
