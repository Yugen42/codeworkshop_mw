package de.conrad.codeworkshop.factory.services.order;

import de.conrad.codeworkshop.factory.services.factory.Worker;
import de.conrad.codeworkshop.factory.services.order.api.Order;
import de.conrad.codeworkshop.factory.services.order.api.OrderConfirmation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Andreas Hartmann
 */
@RestController("orderController")
@RequestMapping("/order")
public class Controller {

    private final Service factoryService;
    @Autowired
    Worker worker;

    @Autowired
    public Controller(final Service service) {
        this.factoryService = service;
    }

    @PostMapping("/create")
    public ResponseEntity<OrderConfirmation> createOrder(@RequestBody final Order order) {
        return ResponseEntity.status(HttpStatus.CREATED).body(factoryService.createOrder(order));
    }
}