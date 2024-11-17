package factory;

import com.gngsn.chapter2.v2.Money;
import com.gngsn.chapter2.v2.Movie;
import com.gngsn.chapter2.v2.discount.policy.AmountDiscountPolicy;

import java.time.Duration;

public class Factory {
    public Movie createAvatarMovie() {
        return new Movie("아바타",
                Duration.ofMinutes(120),
                Money.wons(10000),
                new AmountDiscountPolicy(null, null));
    }
}