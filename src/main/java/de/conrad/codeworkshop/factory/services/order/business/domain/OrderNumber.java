package de.conrad.codeworkshop.factory.services.order.business.domain;

import java.math.BigInteger;
import java.util.Random;

import static java.math.BigInteger.valueOf;

/**
 * @author Andreas Hartmann
 */
public class OrderNumber {
    private final BigInteger orderNumberPlain;

    public OrderNumber(final BigInteger futureOrderNumberPlain) {
        orderNumberPlain = futureOrderNumberPlain;
    }

    public static OrderNumber generate() {
        return new OrderNumber(valueOf(new Random().nextInt()));
    }

    public BigInteger getOrderNumberPlain() {
        return orderNumberPlain;
    }
}
