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
}
