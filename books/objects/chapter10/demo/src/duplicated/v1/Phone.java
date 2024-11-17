package duplicated.v1;

import duplicated.Call;
import duplicated.Money;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * v1. Phone: Call의 목록을 관리할 정보 전문가
 *
 * - amount: 단위 요금 저장
 * - seconds: 단위 시간 저장
 *   - '10 초당 5원'씩 부과되는 요금제 → {amount=5, seconds=10}
 * - calls: 전체 통화 목록 저장
 */
public class Phone {

    private Money amount;
    private Duration seconds;
    private List<Call> calls = new ArrayList<>();

    public Phone(Money amount, Duration seconds) {
        this.amount = amount;
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
            result = result.plus(amount.times(call.getDuration().getSeconds() / seconds.getSeconds()));
        }

        return result;
    }

}