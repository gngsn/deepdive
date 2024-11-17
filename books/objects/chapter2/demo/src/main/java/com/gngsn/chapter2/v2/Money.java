package com.gngsn.chapter2.v2;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Version1. 돈
 */
public class Money {
    public static final Money ZERO = Money.wons(0);

    // 객체를 이용해 도메인의 의미를 풍부하게 표현
    // : long 타입은 변수의 크기나 연산자의 종류와 관된 구현 관점의 제약은 표현할 수 있지만,
    // Money 타입처럼 저장하는 값이 금액과 관련돼 있다는 의미를 전달할 수 없음
    private final BigDecimal amount;

    public Money(BigDecimal amount) {
        this.amount = amount;
    }

    public static Money wons(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    // 클래스에 중첩된 wrapper가 생성되는 건 OOM 위험 +
    public Money plus(Money amount) {
        return new Money(this.amount.add(amount.amount));
    }

    public Money minus(Money amount) {
        return new Money(this.amount.subtract(amount.amount));
    }

    public Money times(double percent) {
        return new Money(this.amount.multiply(
            BigDecimal.valueOf(percent)
        ));
    }

    public boolean isLessThan(Money other) {
        return amount.compareTo(other.amount) < 0;
    }

    public boolean isGreaterThanOrEqual(Money other) {
        return amount.compareTo(other.amount) >= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
