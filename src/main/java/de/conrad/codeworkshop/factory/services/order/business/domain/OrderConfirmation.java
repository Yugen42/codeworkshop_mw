package de.conrad.codeworkshop.factory.services.order.business.domain;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

/**
 * @author Andreas Hartmann
 */
public class OrderConfirmation {

    public static final OrderConfirmation BLANK_ORDER_CONFIRMATION = new OrderConfirmation(null, OrderStatus.DECLINED);

    private OrderStatus status;
    private OrderNumber orderNumber;

    public OrderConfirmation(final OrderNumber orderNumber, final OrderStatus status) {
        this.orderNumber = orderNumber;
        this.status = status;
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
