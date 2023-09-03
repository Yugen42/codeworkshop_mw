package de.conrad.codeworkshop.factory.services.order;

import de.conrad.codeworkshop.factory.services.order.api.Order;
import de.conrad.codeworkshop.factory.services.order.api.OrderConfirmation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Andreas Hartmann
 */
@RestController("orderController")
@RequestMapping("/order")
public class OrderController {

    private final OrderService factoryOrderService;

    @Autowired
    public OrderController(final OrderService orderService) {
        this.factoryOrderService = orderService;
    }

    @PostMapping("/create")
    public OrderConfirmation createOrder(final @RequestBody Order order) {
        return factoryOrderService.createOrder(order);
    }
}