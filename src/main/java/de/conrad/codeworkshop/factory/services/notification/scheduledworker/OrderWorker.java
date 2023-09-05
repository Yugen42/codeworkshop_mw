package de.conrad.codeworkshop.factory.services.notification.scheduledworker;

import de.conrad.codeworkshop.factory.services.factory.FactoryService;
import de.conrad.codeworkshop.factory.services.notification.NotificationService;
import de.conrad.codeworkshop.factory.services.order.business.domain.Order;
import de.conrad.codeworkshop.factory.services.order.business.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * @author Ina Lieder
 */
@Slf4j
@AllArgsConstructor
public class OrderWorker implements Runnable {

    public static final int FIVE_SECONDS = 5000;
    private final FactoryService factoryService;
    private final NotificationService notificationService;
    private Semaphore semaphore;

    @Override
    public void run() {
        final List<Order> inProgressList = new ArrayList<>();
        try {
            semaphore.acquire();
            final Order order = factoryService.getOrderFromQueue();
            if (order != null) {
                inProgressList.add(order);
                log.info(String.format("Worker processing order %d", order.getOrderNumber().getOrderNumberPlain()));
                order.setStatus(OrderStatus.COMPLETED);
                log.info(String.format("Order %d is completed", order.getOrderNumber().getOrderNumberPlain()));
                Thread.sleep(FIVE_SECONDS);
                notificationService.notifyCustomer(order);
                inProgressList.clear();
            } else {
                log.info("Worker processing: no orders in the queue");
            }
        } catch (final Exception ex) {
            log.error("Worker execution failed", ex);
            if (!CollectionUtils.isEmpty(inProgressList)) {
                inProgressList.forEach(order -> {
                    factoryService.addFailedOrderToDeadLetterQueue(order);
                });
            }
        }
        semaphore.release();
    }
}
