package de.conrad.codeworkshop.factory.services.order.business;

import de.conrad.codeworkshop.factory.services.factory.FactoryService;
import de.conrad.codeworkshop.factory.services.order.business.domain.Order;
import de.conrad.codeworkshop.factory.services.order.business.domain.OrderConfirmation;
import de.conrad.codeworkshop.factory.services.order.business.domain.OrderNumber;
import de.conrad.codeworkshop.factory.services.order.business.domain.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * Creates the accepted order: orderNumber is assigned and the order is enqueued in the factory via the factoryService.
     * The order confirmation is returned.
     */
    public OrderConfirmation createOrder(final Order order) {
        order.setOrderNumber(OrderNumber.generate());
        order.setStatus(ACCEPTED);
        log.info(String.format("Order %d was accepted and created", order.getOrderNumber().getOrderNumberPlain()));

        factoryService.enqueue(order);
        return new OrderConfirmation(order.getOrderNumber(), OrderStatus.ACCEPTED);
    }
}
