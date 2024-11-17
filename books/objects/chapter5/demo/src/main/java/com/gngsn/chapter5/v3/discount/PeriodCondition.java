package com.gngsn.chapter5.v3.discount;

import com.gngsn.chapter5.v3.Screening;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * v2. DiscountCondition 분리 - PeriodCondition
 */
public class PeriodCondition implements DiscountCondition {

    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public PeriodCondition(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public boolean isSatisfiedBy(Screening screening) {
        return dayOfWeek.equals(screening.getWhenScreened().getDayOfWeek()) &&
                !startTime.isBefore(screening.getWhenScreened().toLocalTime())&&
                !endTime.isAfter(screening.getWhenScreened().toLocalTime());
    }
}