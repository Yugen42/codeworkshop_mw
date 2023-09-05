package de.conrad.codeworkshop.factory.services.notification;

import de.conrad.codeworkshop.factory.services.order.business.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Andreas Hartmann
 */
@Service
@Slf4j
public class NotificationService {

    public void notifyCustomer(final Order order) {
        // Dummy function that would notify the customer that manufacturing is completed.
        try {
            Thread.sleep(500);
        } catch (final InterruptedException interruptedException) {
            log.error("NotificationService interrupted: ", interruptedException.getMessage());
            throw new RuntimeException("NotificationService error: ", interruptedException);

        }
    }
}
