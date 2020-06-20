package de.conrad.codeworkshop.factory.services.factory;

import de.conrad.codeworkshop.factory.services.order.api.Order;
import de.conrad.codeworkshop.factory.services.notification.Service;
import de.conrad.codeworkshop.factory.services.order.api.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;

@org.springframework.stereotype.Service("workerService")
public class Worker implements Runnable {
    private ConcurrentLinkedQueue<Order> queue;
    final int SLEEP_TIME = 5000;


    @Autowired
    Service getNotificationService;


    public void setQueue(ConcurrentLinkedQueue<Order> queue) {
        this.queue = queue;
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
            getNotificationService.notifyCustomer(currentOrder);
        } catch (final InterruptedException | NoSuchElementException interruptedException) {
            System.err.println(interruptedException.getMessage());
        }
    }

}
