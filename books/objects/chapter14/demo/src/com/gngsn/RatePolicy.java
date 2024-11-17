package com.gngsn;

public interface RatePolicy {
    Money calculateFee(Phone phone);
}