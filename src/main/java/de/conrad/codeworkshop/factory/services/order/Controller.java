package de.conrad.codeworkshop.factory.services.order;

import de.conrad.codeworkshop.factory.services.order.api.Order;
import de.conrad.codeworkshop.factory.services.order.api.OrderConfirmation;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;


/**
 * @author Andreas Hartmann
 */
@RestController("orderController")
@RequestMapping(
				value = "/order",
				consumes = MediaType.APPLICATION_JSON_VALUE,
				produces = MediaType.APPLICATION_JSON_VALUE
				)
public class Controller {

    private final Service factoryService;

    @Autowired	
    public Controller(final Service service) {
        this.factoryService = service;
    }

    @PostMapping("/create")
    public ResponseEntity<OrderConfirmation> createOrder(@Valid @RequestBody final Order order) {
    	OrderConfirmation orderConfirmation =  factoryService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderConfirmation);
        }
}