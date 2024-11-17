package duplicated.v4;

import duplicated.Call;
import duplicated.Money;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * v4. Phone: Call의 목록을 관리할 정보 전문가
 *
 * - PhoneType 타입 코드 추가
 */
public class Phone {

    private static final int LATE_NIGHT_HOUR = 22;

    enum PhoneType {REGULAR, NIGHTLY}

    private PhoneType type;

    private Money amount;

    private Money regularAmount;

    private Money nightlyAmount;

    private Duration seconds;

    private List<Call> calls = new ArrayList<>();

    public Phone(final Money amount, final Duration seconds) {
        this(PhoneType.REGULAR, amount, Money.ZERO, Money.ZERO, seconds);
    }

    public Phone(final Money regularAmount, final Money nightlyAmount, final Duration seconds) {
        this(PhoneType.NIGHTLY, Money.ZERO, regularAmount, nightlyAmount, seconds);
    }

    public Phone(final PhoneType type, final Money amount, final Money regularAmount, final Money nightlyAmount, final Duration seconds) {
        this.type = type;
        this.amount = amount;
        this.regularAmount = regularAmount;
        this.nightlyAmount = nightlyAmount;
        this.seconds = seconds;
    }

    public void call(Call call) {
        calls.add(call);
    }

    public List<Call> getCalls() {
        return calls;
    }

    public Money getAmount() {
        return amount;
    }

    public Duration getSeconds() {
        return seconds;
    }

    public Money calculateFee() {
        Money result = Money.ZERO;

        for (Call call : calls) {
            if (type == PhoneType.REGULAR) {
                result = result.plus(
                        amount.times(call.getDuration().getSeconds() / seconds.getSeconds()));
            } else {
                if (call.getFrom().getHour() >= LATE_NIGHT_HOUR) {
                    result = result.plus(
                            nightlyAmount.times(call.getDuration().getSeconds() / seconds.getSeconds()));
                } else {
                    result = result.plus(
                            regularAmount.times(call.getDuration().getSeconds() / seconds.getSeconds()));
                }
            }
        }
        return result;
    }

}