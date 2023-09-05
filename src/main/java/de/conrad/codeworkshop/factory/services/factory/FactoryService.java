package de.conrad.codeworkshop.factory.services.factory;

import de.conrad.codeworkshop.factory.services.order.business.domain.Order;
import de.conrad.codeworkshop.factory.services.order.business.domain.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Andreas Hartmann
 */
@Slf4j
@Service
public class FactoryService {

    private ConcurrentLinkedQueue<Order> manufacturingQueue = new ConcurrentLinkedQueue<>();
    private List<Order> deadLetterQueue = new ArrayList<>();

    /**
     * Sets the order in progress and enqueues the order in the factory
     */
    public void enqueue(final Order order) {
        order.setStatus(OrderStatus.IN_PROGRESS);
        manufacturingQueue.add(order);
        log.info("Order is set in progress and put in the manufacturing queue");
    }

    public Order getOrderFromQueue() {
        return manufacturingQueue.poll();
    }

    public void addFailedOrderToDeadLetterQueue(final Order order) {
        deadLetterQueue.add(order);
    }

    public List<Order> getDeadLetterQueue() {
        return deadLetterQueue;
    }

    public void clearDeadLetterQueue() {
        deadLetterQueue.clear();
    }

}
