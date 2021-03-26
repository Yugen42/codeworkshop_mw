package de.conrad.codeworkshop.factory.services.order.api;

import static de.conrad.codeworkshop.factory.services.order.api.OrderStatus.ACCEPTED;
import static de.conrad.codeworkshop.factory.services.order.api.OrderStatus.DECLINED;
import static de.conrad.codeworkshop.factory.services.order.api.OrderStatus.PENDING;

import java.util.List;

/**
 * @author Andreas Hartmann
 */
public class Order {
    private List<Position> positions;
    private OrderConfirmation orderConfirmation;
    private OrderStatus status = PENDING;

    public void validate() {
        if (!positions.isEmpty() && status == PENDING) {
            status = ACCEPTED;
        } else {
            status = DECLINED;
        }
    }

    public void setOrderConfirmation(final OrderConfirmation orderConfirmation) {
        this.orderConfirmation = orderConfirmation;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
