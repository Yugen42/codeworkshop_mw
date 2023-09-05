package de.conrad.codeworkshop.factory.services.order.business;

import de.conrad.codeworkshop.factory.services.factory.FactoryService;
import de.conrad.codeworkshop.factory.services.order.business.domain.Order;
import de.conrad.codeworkshop.factory.services.order.business.domain.OrderConfirmation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static de.conrad.codeworkshop.factory.services.order.business.domain.OrderConfirmation.BLANK_ORDER_CONFIRMATION;
import static de.conrad.codeworkshop.factory.services.order.business.domain.OrderStatus.ACCEPTED;

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
        final OrderConfirmation orderConfirmation;

        if (order.getStatus() == ACCEPTED) {
            orderConfirmation = new OrderConfirmation(order.getOrderNumber(), ACCEPTED);
            order.setOrderConfirmation(orderConfirmation);
            factoryService.enqueue(order);
        } else {
            log.warn("createOrder: order declined");
            orderConfirmation = BLANK_ORDER_CONFIRMATION;
        }

        return orderConfirmation;
    }
}
