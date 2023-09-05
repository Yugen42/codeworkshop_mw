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

    private OrderStatus status = OrderStatus.PENDING;


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
