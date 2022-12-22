package de.conrad.codeworkshop.factory.services.order.api;

import static de.conrad.codeworkshop.factory.services.order.api.OrderStatus.ACCEPTED;
import static de.conrad.codeworkshop.factory.services.order.api.OrderStatus.DECLINED;
import static de.conrad.codeworkshop.factory.services.order.api.OrderStatus.PENDING;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * @author Andreas Hartmann
 */
public class Order {
    private static final BigDecimal DIVISOR_VALUE = BigDecimal.valueOf(10);
    private static final BigDecimal EXPECTED_QUANTITY = BigDecimal.valueOf(42.42);
    private static final int MIN_PRODUCT_ID_LENGTH = 6;
    private static final int MAX_PRODUCT_ID_LENGTH = 9;
    private List<Position> positions;
    private OrderConfirmation orderConfirmation;
    private OrderStatus status = PENDING;

    public void validate() {
        if (Objects.isNull(positions)) {
            status = DECLINED;
            return;
        }

        if (!positions.isEmpty()
            && status == PENDING
            && validateProductIdLengthBetween(MIN_PRODUCT_ID_LENGTH, MAX_PRODUCT_ID_LENGTH)
            && validateQuantity()
        ) {
            status = ACCEPTED;
        } else {
            status = DECLINED;
        }
    }

    private boolean validateQuantity() {
        return positions
            .stream()
            .map(Position::getQuantity)
            .allMatch(quantity -> {
                    if (Objects.isNull(quantity)) {
                        return false;
                    }
                    return isQuantityDivisibleBy(quantity, DIVISOR_VALUE)
                        || isQuantityBetween(quantity, BigDecimal.ZERO, BigDecimal.ONE)
                        || isQuantityEquals(quantity, EXPECTED_QUANTITY);
                }
            );
    }

    private boolean isQuantityDivisibleBy(BigDecimal quantity, BigDecimal divisor) {
        return quantity.divide(divisor).stripTrailingZeros().scale() <= 0;
    }

    private boolean isQuantityBetween(BigDecimal quantity, BigDecimal start, BigDecimal end) {
        return quantity.compareTo(start) > 0 && quantity.compareTo(end) < 1;
    }

    private boolean isQuantityEquals(BigDecimal quantity, BigDecimal expectedQuantity) {
        return quantity.equals(expectedQuantity);
    }

    private boolean validateProductIdLengthBetween(int minProductIdLength, int maxProductIdLength) {
        return positions
            .stream()
            .allMatch(position -> {
                if (Objects.isNull(position.getProductId())) {
                    return false;
                }
                String productId = String.valueOf(position.getProductId());
                return productId.length() >= minProductIdLength && productId.length() <= maxProductIdLength;
            });
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
