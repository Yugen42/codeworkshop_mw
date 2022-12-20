package de.conrad.codeworkshop.factory.services.order.api;

import java.util.List;

import static de.conrad.codeworkshop.factory.services.order.api.Conditions.*;
import static de.conrad.codeworkshop.factory.services.order.api.OrderStatus.*;

/**
 * @author Andreas Hartmann
 */
public class Order {
    private List<Position> positions;
    private OrderConfirmation orderConfirmation;
    private OrderStatus status = PENDING;

    public void validate() {
        if (!positions.isEmpty() && (status == PENDING) && checkPositions(positions)) {
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

    private boolean checkPositions(List<Position> positions){
        boolean ret = true;
        for (Position p : positions){
            if (!inRange(p.getProductId())
                    && !(divisibleBy10(p.getQuantity())) || betweenZeroAndOne(p.getQuantity()) || exactly42_42(p.getQuantity())) {
                ret = false;
            }
        }
        return ret;
    }

}
