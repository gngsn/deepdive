package com.gngsn.chapter5.v1.discount;

import com.gngsn.chapter5.v1.Screening;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * v1. 영화 할인 조건
 */
public class DiscountCondition {
    private DiscountConditionType type;
    private int sequence;

    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;


    public DiscountConditionType getType() {
        return type;
    }

    public boolean isSatisfiedBy(Screening screening) {
        if (type == DiscountConditionType.PERIOD) {
            return isSatisfiedByPeriod(screening);
        }
        return isSatisfiedBySequence(screening);
    }

    public boolean isSatisfiedByPeriod(Screening screening) {
        return dayOfWeek.equals(screening.getWhenScreened().getDayOfWeek()) &&
                !startTime.isBefore(screening.getWhenScreened().toLocalTime())&&
                !endTime.isAfter(screening.getWhenScreened().toLocalTime());
    }

    public boolean isSatisfiedBySequence(Screening screening) {
        return sequence == screening.getSequence();
    }

    public int getSequence() {
        return sequence;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}