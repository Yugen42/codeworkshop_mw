package de.conrad.codeworkshop.factory.services.factory;

import de.conrad.codeworkshop.factory.services.order.api.Order;
import de.conrad.codeworkshop.factory.services.notification.Service;
import de.conrad.codeworkshop.factory.services.order.api.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Worker implements Runnable {
    private ConcurrentLinkedQueue<Order> queue = null;
    final int SLEEP_TIME = 5000;

    final Service notificationService;

    @Autowired
    Worker(ConcurrentLinkedQueue<Order> queue, Service notificationService){
        this.notificationService = notificationService;
        run();
    }
    @Override
    public void run(){
/*        synchronized (queue) {
            while (queue.size() == 0){
                try {
                    queue.wait();
                } catch (InterruptedException e){
                    return;
                }
            }
        }*/

        try {
            Order currentOrder = queue.remove();
            currentOrder.setStatus(OrderStatus.COMPLETED);
            Thread.sleep(SLEEP_TIME);
            notificationService.notifyCustomer(currentOrder);
        } catch (final InterruptedException | NoSuchElementException interruptedException) {
            System.err.println(interruptedException.getMessage());
        }
    }

}
