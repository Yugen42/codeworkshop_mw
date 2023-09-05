package de.conrad.codeworkshop.factory.services.notification;

import de.conrad.codeworkshop.factory.services.factory.FactoryService;
import de.conrad.codeworkshop.factory.services.order.business.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Andreas Hartmann
 */
@Service
@Slf4j
public class NotificationService {
    private FactoryService factoryService;

    @Autowired
    public NotificationService(final FactoryService factoryService) {
        this.factoryService = factoryService;
    }

    /**
     * Notifies the customer after the order completion
     */
    public void notifyCustomer(final Order order) {
        try {
            log.info(String.format("Customer notified for order %d", order.getOrderNumber().getOrderNumberPlain()));
        } catch (final Exception ex) {
            factoryService.addFailedOrderToDeadLetterQueue(order);
            log.error("Customer notification failed", ex);
        }
    }
}
