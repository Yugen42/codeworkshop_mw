
package de.conrad.codeworkshop.factory.services.factory;

import de.conrad.codeworkshop.factory.services.order.api.Order;
import org.springframework.scheduling.annotation.Async;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * primitive asynchronous worker that will remove entries from
 * de.conrad.codeworkshop.factory.services.factory.Service#manufacturingQueue,
 * sets their status to COMPLETED, waits for five seconds and then calls
 * de.conrad.codeworkshop.factory.services.notification.Service#notifyCustomer
 * (to notify the customer that their order is completed).
 * 
 * @author Zakaria Ouasbir
 *
 */
@org.springframework.stereotype.Service("worker")
public class FactoryWorker {

	private final int sleepTime = 5000; // millis

	private final de.conrad.codeworkshop.factory.services.factory.Service factoryService;

	private final de.conrad.codeworkshop.factory.services.notification.Service notificationService;

	public FactoryWorker(de.conrad.codeworkshop.factory.services.factory.Service factoryService,
			de.conrad.codeworkshop.factory.services.notification.Service notificationService) {
		this.factoryService = factoryService;
		this.notificationService = notificationService;
	}

	/*
	 * Method to run the Worker in a separate Thread
	 * 
	 * @return CompletableFuture<List<Order>> when it's completed to get result of
	 * orders processing , use .get() retrieve result as : List<Order>
	 */
	@Async
	public CompletableFuture<List<Order>> runWorker() throws InterruptedException {
		System.out.println("Thread Name:" + Thread.currentThread().getName());
		List<Order> result = factoryService.clearQueueAndSetStatusCompleted();
		try {
			Thread.sleep(sleepTime);
		} catch (final InterruptedException interruptedException) {
			System.err.println(interruptedException.getMessage());
		}
		result.forEach(notificationService::notifyCustomer);
		return CompletableFuture.completedFuture(result);
	}

}
