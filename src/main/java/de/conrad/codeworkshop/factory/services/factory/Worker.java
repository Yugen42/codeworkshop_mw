package de.conrad.codeworkshop.factory.services.factory;

import de.conrad.codeworkshop.factory.services.order.api.Order;
import de.conrad.codeworkshop.factory.services.order.api.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class Worker implements Runnable {

    final int SLEEP_TIME_IN_SECONDS = 5;

    private final Service manufacturingService;
    private final de.conrad.codeworkshop.factory.services.notification.Service notificationService;

    @Autowired
    public Worker(Service manufacturingService,
                  de.conrad.codeworkshop.factory.services.notification.Service notificationService) {
        this.manufacturingService = manufacturingService;
        this.notificationService = notificationService;
        this.run();
    }

    @Override
    public void run() {
        List<Order> orderList = manufacturingService.removeAllFromQueue();

        orderList.forEach(o -> o.setStatus(OrderStatus.COMPLETED));

        try {
            TimeUnit.SECONDS.sleep(SLEEP_TIME_IN_SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        orderList.forEach(o -> notificationService.notifyCustomer(o));
    }
}
