package de.conrad.codeworkshop.factory.services.order;

import de.conrad.codeworkshop.factory.services.order.api.Order;
import de.conrad.codeworkshop.factory.services.order.api.OrderConfirmation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Andreas Hartmann
 */
@RestController("orderController")
@RequestMapping(path = "/order", consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
public class Controller {

    private final Service factoryService;

    @Autowired
    public Controller(final Service service) {
        this.factoryService = service;
    }

    @RequestMapping("/create")
    public OrderConfirmation createOrder(final Order order) {
        return factoryService.createOrder(order);
    }
}