package de.conrad.codeworkshop.factory.services.factory;

import de.conrad.codeworkshop.factory.services.order.api.Order;
import de.conrad.codeworkshop.factory.services.order.api.OrderStatus;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Andreas Hartmann
 */
@org.springframework.stereotype.Service("factoryService")
public class Service {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private ConcurrentLinkedQueue<Order> manufacturingQueue = new ConcurrentLinkedQueue<>();

    void enqueue(final Order order) {
        order.setStatus(OrderStatus.IN_PROGRESS);
        manufacturingQueue.add(order);
    }

    public ConcurrentLinkedQueue<Order> getManufacturingQueue() {
        return manufacturingQueue;
    }
}
