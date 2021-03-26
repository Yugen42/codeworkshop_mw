package de.conrad.codeworkshop.factory.services.order.api;

import static java.math.BigInteger.valueOf;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author Andreas Hartmann
 */
public class OrderNumber {
    private final BigInteger orderNumberPlain;

    public OrderNumber(BigInteger futureOrderNumberPlain) {
        orderNumberPlain = futureOrderNumberPlain;
    }

    public static OrderNumber generate() {
        return new OrderNumber(valueOf(new Random().nextInt()));
    }

    public BigInteger getOrderNumberPlain() {
        return orderNumberPlain;
    }
}
