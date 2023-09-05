package de.conrad.codeworkshop.factory.services.order.business.domain;

import java.math.BigInteger;
import java.util.Random;

import static java.math.BigInteger.valueOf;

/**
 * @author Andreas Hartmann
 */
public class OrderNumber {
    public static final int MIN_PRODUCTID_NUMBER = 100000;
    public static final int MAX_PRODUCTID_NUMBER = 999999999;
    private final BigInteger orderNumberPlain;

    public OrderNumber(final BigInteger futureOrderNumberPlain) {
        orderNumberPlain = futureOrderNumberPlain;
    }

    public static OrderNumber generate() {
        return new OrderNumber(valueOf(generateRandomIntInAllowedRange()));
    }

    public BigInteger getOrderNumberPlain() {
        return orderNumberPlain;
    }

    private static int generateRandomIntInAllowedRange() {
        final Random r = new Random();
        // MAX - MIN + 1  creates a random int in the range of 0 and max, including max.
        // adding MIN to it sets the range for the result between MIN and MAX including MAX
        return r.nextInt(MAX_PRODUCTID_NUMBER - MIN_PRODUCTID_NUMBER + 1) + MIN_PRODUCTID_NUMBER;
    }
}
