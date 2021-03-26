package de.conrad.codeworkshop.factory.services.processing;

import de.conrad.codeworkshop.factory.services.notification.Service;
import de.conrad.codeworkshop.factory.services.order.api.Order;
import de.conrad.codeworkshop.factory.services.order.api.OrderStatus;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderProcessingService {
    private final ConcurrentLinkedQueue<Order> queue;
    private final Service notificationService;
    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    public OrderProcessingService(
            Service notificationService,
            ConcurrentLinkedQueue queue
    ) {
        this.queue = queue;
        this.notificationService = notificationService;
    }

    public void completeAndNotify() {
        CompletableFuture.runAsync(
                () -> {
                    synchronized (queue) {
                        while (!queue.isEmpty()) {
                            Order order = queue.poll();
                            if (order != null) {
                                order.setStatus(OrderStatus.COMPLETED);
                                try {
                                    wait(5000);
                                    notificationService.notifyCustomer(order);
                                } catch (InterruptedException e) {
                                    queue.add(order);
                                    throw new RuntimeException("Can't notify customer");
                                }
                            }
                        }
                    }
                },
                threadPool
        )
                .join();
    }
}

