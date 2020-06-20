package de.conrad.codeworkshop.factory.services.factory;

import de.conrad.codeworkshop.factory.services.order.api.Order;
import de.conrad.codeworkshop.factory.services.order.api.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Andreas Hartmann
 */
@org.springframework.stereotype.Service("factoryService")
class Service {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private ConcurrentLinkedQueue<Order> manufacturingQueue = new ConcurrentLinkedQueue<>();

    @Autowired
    Worker worker;

    void enqueue(final Order order) {
        order.setStatus(OrderStatus.IN_PROGRESS);
        manufacturingQueue.add(order);
    }
    void startWorker(){
        worker.setQueue(manufacturingQueue);
        worker.run();

    }

}
