package de.conrad.codeworkshop.factory.services.processing;

import de.conrad.codeworkshop.factory.services.factory.Service;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableAsync
@Configuration
public class OrderProcessingConfiguration {

    @Component
    private static class ScheduleConfigurator {
        private final de.conrad.codeworkshop.factory.services.notification.Service notificationService;
        private final de.conrad.codeworkshop.factory.services.factory.Service factoryService;

        public ScheduleConfigurator(de.conrad.codeworkshop.factory.services.notification.Service notificationService, Service factoryService) {
            this.notificationService = notificationService;
            this.factoryService = factoryService;
        }

        @Async
        @Scheduled(initialDelay = 400, fixedDelay = 1000)
        public void orderProcessingService() {
            new OrderProcessingService(
                    notificationService,
                    factoryService.getManufacturingQueue()
            ).completeAndNotify();
        }
    }

}
