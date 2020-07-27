package de.conrad.codeworkshop.factory.services.order.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static de.conrad.codeworkshop.factory.services.order.api.OrderStatus.*;

/**
 * @author Andreas Hartmann
 */
public class Order {
    private List<Position> positions;
    private OrderConfirmation orderConfirmation;
    private OrderStatus status = PENDING;

    public void validate() {
        if (Objects.nonNull(positions)
                && !positions.isEmpty()
                && status == PENDING
                && validateAllPositionProductIdBetween6and9digits()
                && validateQuantity()
        ) {
            status = ACCEPTED;
        } else {
            status = DECLINED;
        }
    }

    private boolean validateQuantity() {
        return validateIsDivisibleBy10()
                || validateLarger0Less1()
                || validateIsEqual42_42()
                ;
    }

    private boolean validateIsEqual42_42() {
        return positions
                .stream()
                .allMatch(p ->
                        p.getQuantity().equals(BigDecimal.valueOf(42.42))
                );
    }

    private boolean validateLarger0Less1() {
        return positions
                .stream()
                .allMatch(p ->
                        p.getQuantity().compareTo(BigDecimal.valueOf(0))>0
                                & p.getQuantity().compareTo(BigDecimal.valueOf(1))<1
                );
    }

    private boolean validateIsDivisibleBy10() {
        return positions
                .stream()
                .allMatch(p ->
                        Objects.nonNull(p.getQuantity())
                                && p.getQuantity().divide(BigDecimal.valueOf(10)).stripTrailingZeros().scale() <= 0
                );
    }

    private boolean validateAllPositionProductIdBetween6and9digits() {
        return positions
                .stream()
                .allMatch(p ->
                        Objects.nonNull(p.getProductId())
                                && p.getProductId().toString().length() >= 6
                                & p.getProductId().toString().length() <= 9);
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
