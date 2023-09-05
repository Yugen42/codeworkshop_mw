package de.conrad.codeworkshop.factory.services.notification.scheduledworker;

import de.conrad.codeworkshop.factory.services.factory.FactoryService;
import de.conrad.codeworkshop.factory.services.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ScheduledOrderWorkerExecutor {
    public static final int TIMEOUT = 5;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    final Semaphore binarySemaphore = new Semaphore(1);

    @Autowired
    public ScheduledOrderWorkerExecutor(final FactoryService factoryService, final NotificationService notificationService) {
        final OrderWorker worker = new OrderWorker(factoryService, notificationService, binarySemaphore);
        scheduler.scheduleAtFixedRate(worker, 100, 500, TimeUnit.MILLISECONDS);
        log.info("Started order worker execution");
    }

    public void awaitTermination() {
        try {
            scheduler.awaitTermination(TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            throw new RuntimeException(e);
        }
    }
}

