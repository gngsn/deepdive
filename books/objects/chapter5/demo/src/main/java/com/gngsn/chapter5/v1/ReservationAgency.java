package com.gngsn.chapter5.v1;

import com.gngsn.chapter5.v1.discount.DiscountCondition;
import com.gngsn.chapter5.v1.discount.DiscountConditionType;

public class ReservationAgency {
    public Reservation reserve(Screening screening, Customer customer, int audienceCount) {
        Movie movie = screening.getMovie();

        // PORTION 1. 할인 가능 여부 확인
        boolean discountable = false;
        for (DiscountCondition condition : movie.getDiscountConditions()) {
            if (condition.getType() == DiscountConditionType.PERIOD) {
                discountable = screening.getWhenScreened().getDayOfWeek().equals(condition.getDayOfWeek()) &&
                        !condition.getStartTime().isAfter(screening.getWhenScreened().toLocalTime()) &&
                        !condition.getEndTime().isBefore(screening.getWhenScreened().toLocalTime());
            } else {
                discountable = condition.getSequence() == screening.getSequence();
            }

            if (discountable) {
                break;
            }
        }

        // PORTION 2. discoutable 변수 값 체크 - 할인 정책 계산
        Money fee;
        if (discountable) {
            Money discountAmount = switch (movie.getMovieType()) {
                case AMOUNT_DISCOUNT -> movie.getDiscountAmount();
                case PERCENT_DISCOUNT -> movie.getFee().times(movie.getDiscountPercent());
                case NONE_DISCOUNT -> Money.ZERO;
                default -> Money.ZERO;
            };

            fee = movie.getFee().minus(discountAmount);
        } else {
            fee = movie.getFee();
        }

        return new Reservation(customer, screening, fee, audienceCount);
    }
}
