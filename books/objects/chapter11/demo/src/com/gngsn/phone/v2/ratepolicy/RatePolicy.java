package com.gngsn.phone.v2.ratepolicy;

import com.gngsn.phone.Money;
import com.gngsn.phone.v2.Phone;

public interface RatePolicy {

    Money calculateFee(Phone phone);

}