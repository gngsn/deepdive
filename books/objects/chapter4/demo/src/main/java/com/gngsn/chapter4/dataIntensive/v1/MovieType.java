package com.gngsn.chapter4.dataIntensive.v1;

/**
 * v. Designing Data-Intensive Solutions.
 * 영화 할인 정책 종류 열거형
 */
public enum MovieType {
    AMOUNT_DISCOUNT,    // 금액 할인 정책
    PERCENT_DISCOUNT,   // 비율 할인 정책
    NONE_DISCOUNT,      // 미적용
}