package com.gngsn.appxb.phone;

import com.gngsn.phone.Call;
import com.gngsn.phone.Money;

import java.time.Duration;

public class NightlyDiscountPhone extends Phone {

    private static final int LATE_NIGHT_HOUR = 22;

    private Money nightlyAmount;

    public NightlyDiscountPhone(Money nightlyAmount, Money regularAmount, Duration seconds) {
        super(regularAmount, seconds);
        this.nightlyAmount = nightlyAmount;
    }

    /**
     * 부모 클래스인 Phone의 `calculateFee` 메서드의 구체적인 내부 구현에 강하게 결합
     */
    @Override
    public Money calculateFee() {
        Money result = super.calculateFee();
        for(Call call : getCalls()) {
            if (call.getFrom().getHour() >= LATE_NIGHT_HOUR) {
                result = result.minus(getAmount().minus(nightlyAmount).times(
                        call.getDuration().getSeconds() / getSeconds().getSeconds()));
            }
        }
        return result;
    }
}
