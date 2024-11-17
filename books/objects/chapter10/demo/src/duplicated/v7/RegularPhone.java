package duplicated.v7;

import duplicated.Call;
import duplicated.Money;

import java.time.Duration;

/**
 * v7. Phone: Call의 목록을 관리할 정보 전문가
 */
public class RegularPhone extends AbstractPhone {

    private Money amount;
    private Duration seconds;

    public RegularPhone(Money amount, Duration seconds, double taxRate) {
        super(taxRate);
        this.amount = amount;
        this.seconds = seconds;
    }

    @Override
    protected Money calculateCallFee(Call call) {
        return amount.times(call.getDuration().getSeconds() / seconds.getSeconds());
    }
}
