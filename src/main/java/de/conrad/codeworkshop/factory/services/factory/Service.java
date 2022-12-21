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

    ScheduledExecutorService executorService;

    private final de.conrad.codeworkshop.factory.services.notification.Service notificationService;

    @Autowired
    Service(de.conrad.codeworkshop.factory.services.notification.Service notificationService) {
        this.notificationService = notificationService;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
    }


    void enqueue(final Order order) {
        order.setStatus(OrderStatus.IN_PROGRESS);
        manufacturingQueue.add(order);
    }

    void startEndlessCycle(){
        while(true){
            Order newOrder = manufacturingQueue.poll();
            new Thread(() -> {
                if(newOrder != null) {
                    executorService.schedule(onEvent(newOrder), 5, TimeUnit.SECONDS);
                }
            }).start();
        }
    }

    /**
     * Another way to keep pauses during execution, less recommended due to impact on processing of other requests
     */
    void startPrimivitiveWorker(){
        while(true){
            Order newOrder = manufacturingQueue.poll();
            try {
                TimeUnit.SECONDS.sleep(5);
                onEvent(newOrder);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }

    Runnable onEvent(Order order){
        if (order != null) {
            order.setStatus(OrderStatus.COMPLETED);
        }
        notificationService.notifyCustomer(order);
        return null;
    }
}
