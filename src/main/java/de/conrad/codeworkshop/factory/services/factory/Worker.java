package de.conrad.codeworkshop.factory.services.factory;

import de.conrad.codeworkshop.factory.services.order.api.Order;
import de.conrad.codeworkshop.factory.services.order.api.OrderStatus;
import java.util.List;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Worker {
    public static final int WAIT_MILLIS_BEFORE_NOTIFY_CUSTOMER = 5000;
    public static final int FIXED_RATE_FOR_WORKER = 1000;
    private final de.conrad.codeworkshop.factory.services.notification.Service notificationService;
    private final de.conrad.codeworkshop.factory.services.factory.Service factoryService;

    public Worker(de.conrad.codeworkshop.factory.services.notification.Service notificationService,
                  de.conrad.codeworkshop.factory.services.factory.Service factoryService) {
        this.notificationService = notificationService;
        this.factoryService = factoryService;
    }

    @Async
    @Scheduled(fixedRate = FIXED_RATE_FOR_WORKER)
    public void processOrders() {
        List<Order> ordersToProcess = factoryService.getOrdersToProcess();
        if (!ordersToProcess.isEmpty()) {
            setStatusToCompleted(ordersToProcess);
            try {
                Thread.sleep(WAIT_MILLIS_BEFORE_NOTIFY_CUSTOMER);
            } catch (InterruptedException e) {
                throw new RuntimeException("Something was wrong before notify customer", e);
            }
            notifyCustomer(ordersToProcess);
        }
    }

    private void setStatusToCompleted(List<Order> orders) {
        orders.forEach(order -> order.setStatus(OrderStatus.COMPLETED));
    }

    private void notifyCustomer(List<Order> orders) {
        orders.forEach(notificationService::notifyCustomer);
    }

}
