package de.conrad.codeworkshop.factory.services.factory;

import de.conrad.codeworkshop.factory.services.order.api.Order;
import de.conrad.codeworkshop.factory.services.order.api.OrderStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Andreas Hartmann
 */
@org.springframework.stereotype.Service("factoryService")
class Service {

	@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
	private ConcurrentLinkedQueue<Order> manufacturingQueue = new ConcurrentLinkedQueue<>();


	

	void enqueue(final Order order) {
		order.setStatus(OrderStatus.IN_PROGRESS);
		manufacturingQueue.add(order);
	}

	/*
	 * Method to duplicate Orders to List then Clear manufacturingQueue & finally
	 * Sets status to COMPLETED
	 * 
	 * @return List<Order>
	 */
	List<Order> clearQueueAndSetStatusCompleted() {
		List<Order> backupOrders = new ArrayList<>(manufacturingQueue);
		manufacturingQueue.clear();
		return updateStatusToCompleted.apply(backupOrders);
	}

	/*
	 * Function to set Status to Completed
	 * 
	 * @return List<Order>
	 */
	Function<List<Order>, List<Order>> updateStatusToCompleted = orders -> orders.stream().map(Order::completedOrder)
			.collect(Collectors.toList());

}
