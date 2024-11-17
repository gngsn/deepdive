package com.gngsn.v2.condition;

import com.gngsn.Call;
import com.gngsn.DateTimeInterval;

import java.util.List;

public interface FeeCondition {
    List<DateTimeInterval> findTimeIntervals(Call call);
}
