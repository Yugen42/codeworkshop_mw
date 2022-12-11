package de.conrad.codeworkshop.factory.services.order.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static de.conrad.codeworkshop.factory.services.order.api.OrderStatus.*;

/**
 * @author Andreas Hartmann
 */
public class Order {
	private List<Position> positions;
	private OrderConfirmation orderConfirmation;
	private OrderStatus status = PENDING;

	/*
	 * Method to validate Orders as given : the productId has between 6 and 9 digits
	 * (including) and the quantity is either divisible by 10 or less than one and
	 * larger than 0 or exactly 42.42.
	 * 
	 * @return CompletableFuture<List<Order>> when it's completed to get result of
	 * execution use .get() : List<Order>
	 */
	public void validate() {

		if (positions != null && !positions.isEmpty() && status == PENDING) {
			if (positions.stream().filter(Objects::nonNull)
					.filter(item -> item.getProductId() >= 100000 && item.getProductId() <= 999999999)
					.filter(item -> (item.getQuantity().compareTo(BigDecimal.ONE) < 0
							&& item.getQuantity().compareTo(BigDecimal.ZERO) > 0
							|| item.getQuantity().compareTo(BigDecimal.valueOf(42.42)) == 0
							|| item.getQuantity().doubleValue() % 10 == 0))
					.collect(Collectors.toList()).size() == positions.size())
				status = ACCEPTED;

		} else {
			status = DECLINED;
		}
	}

	/*
	 * Method to set Order's status to COMPLETED :
	 * 
	 * @params : instance Order
	 * 
	 * @return given order with Status : COMPLETED
	 */
	public static Order completedOrder(Order order) {
		order.setStatus(OrderStatus.COMPLETED);
		return order;
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
