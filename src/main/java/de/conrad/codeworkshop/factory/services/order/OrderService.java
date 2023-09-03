package de.conrad.codeworkshop.factory.services.order;

import de.conrad.codeworkshop.factory.services.factory.FactoryService;
import de.conrad.codeworkshop.factory.services.order.api.Order;
import de.conrad.codeworkshop.factory.services.order.api.OrderConfirmation;
import de.conrad.codeworkshop.factory.services.order.api.OrderNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Random;

import static de.conrad.codeworkshop.factory.services.order.api.OrderConfirmation.BLANK_ORDER_CONFIRMATION;
import static de.conrad.codeworkshop.factory.services.order.api.OrderStatus.ACCEPTED;

/**
 * @author Andreas Hartmann
 */
@Slf4j
@Service
public class OrderService {

    private FactoryService factoryService;

    @Autowired
    public OrderService(final FactoryService factoryService) {
        this.factoryService = factoryService;
    }

    /**
     * Validates the input order. If it is valid, it is enqueued in the factory via the factoryController. Either way an
     * appropriate order confirmation is returned.
     */
    public OrderConfirmation createOrder(final Order order) {
        order.validate();

        final OrderConfirmation orderConfirmation;

        if (order.getStatus() == ACCEPTED) {
            orderConfirmation = new OrderConfirmation(new OrderNumber(BigInteger.valueOf(new Random().nextInt())));
            order.setOrderConfirmation(orderConfirmation);
            factoryService.enqueue(order);
        } else {
            log.warn("createOrder: order declined");
            orderConfirmation = BLANK_ORDER_CONFIRMATION;
        }

        return orderConfirmation;
    }
}
