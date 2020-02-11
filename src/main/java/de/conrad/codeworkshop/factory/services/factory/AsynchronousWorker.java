package de.conrad.codeworkshop.factory.services.factory;

import de.conrad.codeworkshop.factory.services.notification.ServiceNotification;
import de.conrad.codeworkshop.factory.services.order.api.Order;
import de.conrad.codeworkshop.factory.services.order.api.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AsynchronousWorker extends Thread{

    @Autowired
    @Qualifier("factoryService")
    private ServiceQueue serviceQueue;

    @Autowired
    private ServiceNotification serviceNotification;

    @Override
    public void run() {
        while (true) {
            Order order = serviceQueue.dequeue();
            if (null != order) {
                order.setStatus(OrderStatus.COMPLETED);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                serviceNotification.notifyCustomer(order);
            }
        }
    }
}
