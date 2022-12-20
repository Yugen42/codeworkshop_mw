package de.conrad.codeworkshop.factory.services.order.api;

import java.math.BigDecimal;

/**
 * in future could be divided on two classes: OrderConditions and PositionConditions for clarity
 */
public class Conditions {

    private static final BigDecimal TEN = new BigDecimal("10");
    private static final BigDecimal ZERO = new BigDecimal("0");
    private static final BigDecimal ONE = new BigDecimal("1");
    private static final BigDecimal EXACT_42_42 = new BigDecimal("42.42");

    static boolean inRange(Integer number){
        return number >= 999_999 && number <= 999_999_999;
    }

    static boolean divisibleBy10(BigDecimal quotient){
        return quotient.remainder(TEN).compareTo(ZERO) == 0;
    }

    static boolean betweenZeroAndOne(BigDecimal amount){
        return amount.compareTo(ZERO) > 0 && amount.compareTo(ONE) < 0;
    }

    static boolean exactly42_42(BigDecimal amount){
        return amount.compareTo(EXACT_42_42) == 0;
    }
}
