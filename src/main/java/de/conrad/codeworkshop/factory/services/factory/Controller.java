package de.conrad.codeworkshop.factory.services.factory;

import de.conrad.codeworkshop.factory.services.order.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Andreas Hartmann
 */
@RestController("factoryController")
@RequestMapping("/factory")
public class Controller {

	private final Service factoryService;

	@Autowired
	public Controller(final Service factoryService) {

		this.factoryService = factoryService;
	}

	@Autowired
	FactoryWorker factoryWorker;

	@PostMapping(value = "/enqueue")
	public final HttpStatus enqueue(final Order order) {
		HttpStatus response = OK;
		try {
			factoryService.enqueue(order);
		} catch (final Exception exception) {
			response = INTERNAL_SERVER_ERROR;
		}
		return response;
	}

	@GetMapping(value = "/completed")
	public final ResponseEntity<List<Order>> cleanQueue()
			throws InterruptedException, ExecutionException {
		List<Order> orders = this.factoryWorker.runWorker().get();
		return ResponseEntity.status(HttpStatus.CREATED).body(orders);
	}

}
