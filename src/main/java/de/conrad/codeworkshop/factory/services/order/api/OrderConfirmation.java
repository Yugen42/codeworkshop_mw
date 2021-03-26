package de.conrad.codeworkshop.factory.services.order.api;

import static de.conrad.codeworkshop.factory.services.order.api.OrderNumber.generate;
import static de.conrad.codeworkshop.factory.services.order.api.OrderStatus.ACCEPTED;
import static de.conrad.codeworkshop.factory.services.order.api.OrderStatus.DECLINED;

import javax.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

/**
 * @author Andreas Hartmann
 */
public class OrderConfirmation {

    public static final OrderConfirmation BLANK_ORDER_CONFIRMATION = new OrderConfirmation(null);

    private OrderStatus status;
    private OrderNumber orderNumber;

    public OrderConfirmation(final OrderNumber orderNumber) {
        this.orderNumber = generate();

        this.status = null == orderNumber ? DECLINED : ACCEPTED;
    }

    @Nullable
    public OrderNumber getOrderNumber() {
        return orderNumber;
    }

    @NotNull
    public OrderStatus getStatus() {
        return status;
    }
}
