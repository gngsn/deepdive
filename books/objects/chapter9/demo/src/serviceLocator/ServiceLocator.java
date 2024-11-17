package serviceLocator;

import com.gngsn.chapter2.v2.discount.policy.DiscountPolicy;

public class ServiceLocator {
    private static ServiceLocator soleInstance = new ServiceLocator();

    private DiscountPolicy discountPolicy;

    public static DiscountPolicy discountPolicy() {
        return soleInstance.discountPolicy;
    }

    public static void provide(DiscountPolicy discountPolicy) {
        soleInstance.discountPolicy = discountPolicy;
    }

    private ServiceLocator() {
    }
}