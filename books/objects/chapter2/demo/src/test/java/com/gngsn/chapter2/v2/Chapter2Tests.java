package com.gngsn.chapter2.v2;

import com.gngsn.chapter2.v2.discount.condition.PeriodCondition;
import com.gngsn.chapter2.v2.discount.condition.SequenceCondition;
import com.gngsn.chapter2.v2.discount.policy.AmountDiscountPolicy;
import com.gngsn.chapter2.v2.discount.policy.NoneDiscountPolicy;
import com.gngsn.chapter2.v2.discount.policy.PercentDiscountPolicy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;

@DisplayName("Movie Class V2")
class Chapter2Tests {

    private final Movie titanic = new Movie("타이타닉",
        Duration.ofMinutes(180),
        Money.wons(11_000),
        new PercentDiscountPolicy(0.1,
            new SequenceCondition(2),
            new PeriodCondition(DayOfWeek.TUESDAY, LocalTime.of(14, 0), LocalTime.of(16, 59)),
            new PeriodCondition(DayOfWeek.THURSDAY, LocalTime.of(10, 0), LocalTime.of(13, 59))
        )
    );

    private final Movie starWars = new Movie("스타워즈",
        Duration.ofMinutes(210),
        Money.wons(10_000),
        new NoneDiscountPolicy()
    );

    @Nested
    @DisplayName("Avatar Class")
    class Avatar {

        private final BigDecimal discountFee = new BigDecimal(800);
        private final Movie avatar = new Movie("아바타",
            Duration.ofMinutes(120),
            Money.wons(10_000),
            new AmountDiscountPolicy(Money.wons(discountFee.longValue()),
                new SequenceCondition(1),
                new SequenceCondition(10),
                new PeriodCondition(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 59)),
                new PeriodCondition(DayOfWeek.THURSDAY, LocalTime.of(10, 0), LocalTime.of(20, 59))
            )
        );

        @Test
        void discounted_cost() {
            final LocalDate monday = LocalDate.of(2023, 4, 17);
            List<Screening> discountedScreeningList = List.of(
                new Screening(avatar, 1, LocalDateTime.of( 2023, 4,19, 8, 59)),
                new Screening(avatar, 10, LocalDateTime.of( 2023, 4,19, 9, 30)),
                new Screening(avatar, 9999, monday.atTime(11, 59)),
                new Screening(avatar, 9999, monday.plusDays(3).atTime(20, 59))
            );

            for (Screening discountingScreening : discountedScreeningList) {
                final Money calculated = avatar.calculateMovieFee(discountingScreening);

                Assertions.assertEquals(avatar.getFee().minus(new Money(discountFee)), calculated);
            }
        }

        @Test
        void not_discounted_cost() {
            List<Screening> discountedScreeningList = List.of(
                new Screening(avatar, 2, LocalDateTime.of( 2023, 4,19, 8, 59))
            );

            for (Screening discountingScreening : discountedScreeningList) {
                final Money calculated = avatar.calculateMovieFee(discountingScreening);

                Assertions.assertEquals(avatar.getFee(), calculated);
            }
        }

    }
}