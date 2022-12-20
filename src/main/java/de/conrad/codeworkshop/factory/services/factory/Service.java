package de.conrad.codeworkshop.factory.services.factory;

import de.conrad.codeworkshop.factory.services.order.api.Order;
import de.conrad.codeworkshop.factory.services.order.api.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Hartmann
 */
@org.springframework.stereotype.Service("factoryService")
class Service {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final ConcurrentLinkedQueue<Order> manufacturingQueue = new ConcurrentLinkedQueue<>();

    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private final de.conrad.codeworkshop.factory.services.notification.Service notificationService;

    @Autowired
    Service(de.conrad.codeworkshop.factory.services.notification.Service notificationService) {
        this.notificationService = notificationService;
    }


    void enqueue(final Order order) {
        order.setStatus(OrderStatus.IN_PROGRESS);
        manufacturingQueue.add(order);
    }

    void startEndlessCycle(){
        while(true){
            Order newOrder = manufacturingQueue.poll();
            new Thread(() -> {
                assert newOrder != null;
                executorService.schedule(onEvent(newOrder), 5, TimeUnit.SECONDS);
            }).start();
        }
    }

    Runnable onEvent(Order order){
        assert order != null;
        order.setStatus(OrderStatus.COMPLETED);
        notificationService.notifyCustomer(order);
        return null;
    }
}
