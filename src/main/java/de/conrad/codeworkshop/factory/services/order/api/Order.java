package de.conrad.codeworkshop.factory.services.order.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static de.conrad.codeworkshop.factory.services.order.api.OrderStatus.*;

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
            for (Position position: positions) {

                /**
                 * Rule:
                 * productId has between 6 and 9 digits (including)
                 */
                Integer productIdSize = position.getProductId().toString().length();
                if (!(productIdSize >= 6 && productIdSize <= 9)){
                    status = DECLINED;
                }

                /**
                 * Rule:
                 * quantity is either
                 * * divisible by 10
                 * * or less than one and larger than 0
                 * * or exactly 42.42.
                 */
                BigDecimal bigDecimal = position.getQuantity();
                Pattern pattern = Pattern.compile("^(-?\\d*0|(0+\\.?|0*\\.\\d+|0*1(\\.0*)?)|42.42)$");
                Matcher matcher = pattern.matcher(bigDecimal.toString());
                if (!matcher.matches()){
                    status = DECLINED;
                }
            }

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
