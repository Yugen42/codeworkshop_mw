package de.conrad.codeworkshop.factory.services.notification;

import de.conrad.codeworkshop.factory.FactoryApplication;
import de.conrad.codeworkshop.factory.services.factory.FactoryService;
import de.conrad.codeworkshop.factory.services.order.business.domain.Order;
import de.conrad.codeworkshop.factory.services.order.business.domain.OrderNumber;
import de.conrad.codeworkshop.factory.services.order.business.domain.OrderStatus;
import de.conrad.codeworkshop.factory.services.order.business.domain.Position;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;

import static java.lang.Thread.sleep;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@SpringBootTest(classes = FactoryApplication.class)
@AutoConfigureMockMvc
public class OrderWorkerTest {

    @Autowired
    private FactoryService factoryService;

    @MockBean
    private NotificationService notificationService;

    @Test
    public void orderWorkerShouldProcessOrderCorrectly() {
        final Order order = makeOrder();
        factoryService.clearDeadLetterQueue();
        order.setStatus(OrderStatus.ACCEPTED);
        order.setOrderNumber(OrderNumber.generate());
        factoryService.clearDeadLetterQueue();
        factoryService.enqueue(order);
        try {
            sleep(6000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETED);
        assertThat(factoryService.getOrderFromQueue()).isNull();
        assertThat(factoryService.getDeadLetterQueue()).isEmpty();
        verify(notificationService, times(1)).notifyCustomer((any()));
    }

    @Test
    public void workerShouldAddInvalidOrderToDeadLetterQueue() {
        final Order order = makeOrder(); // order without orderNumber should cause exception
        order.setStatus(OrderStatus.ACCEPTED);
        factoryService.clearDeadLetterQueue();
        factoryService.enqueue(order);
        try {
            sleep(6000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertThat(factoryService.getDeadLetterQueue().size()).isEqualTo(1);
        assertThat(factoryService.getOrderFromQueue()).isNull();
        verify(notificationService, times(0)).notifyCustomer((any()));
    }

    private static Order makeOrder() {
        final Order order = new Order();
        order.setPositions(new ArrayList<>());
        final Position position = new Position();
        position.setProductId(100000);
        position.setQuantity(BigDecimal.valueOf(42.42));
        order.getPositions().add(position);
        return order;
    }
}
