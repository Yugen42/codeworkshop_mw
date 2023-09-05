package de.conrad.codeworkshop.factory.services.order.business.domain;

import lombok.Data;

import java.util.List;


/**
 * @author Andreas Hartmann
 */
@Data
public class Order {
    private OrderNumber orderNumber;

    private List<Position> positions;
    private OrderConfirmation orderConfirmation;

    private OrderStatus status = OrderStatus.PENDING;

  /*  public void validate() {
        if (!positions.isEmpty() && status == PENDING) {
            status = ACCEPTED;
        } else {
            status = DECLINED;
        }
    }*/

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
