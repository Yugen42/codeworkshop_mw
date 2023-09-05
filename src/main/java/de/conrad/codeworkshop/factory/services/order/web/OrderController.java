package de.conrad.codeworkshop.factory.services.order.web;

import de.conrad.codeworkshop.factory.services.order.business.OrderService;
import de.conrad.codeworkshop.factory.services.order.web.mapping.DtoToDomainMapper;
import de.conrad.codeworkshop.factory.services.order.web.api.OrderDto;
import de.conrad.codeworkshop.factory.services.order.business.domain.Order;
import de.conrad.codeworkshop.factory.services.order.business.domain.OrderConfirmation;
import de.conrad.codeworkshop.factory.services.order.web.validation.OrderDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Andreas Hartmann
 */
@RestController("orderController")
@RequestMapping("/order")
@Validated
public class OrderController {

    private final OrderService factoryOrderService;
    private final DtoToDomainMapper dtoToDomainMapper;

    @Autowired
    public OrderController(final OrderService orderService, final DtoToDomainMapper dtoToDomainMapper) {
        this.factoryOrderService = orderService;
        this.dtoToDomainMapper = dtoToDomainMapper;
    }

    @PostMapping(
        value = {"/create"},
        produces = {"application/json;charset=UTF-8"},
        consumes = {"application/json;charset=UTF-8"}
    )
    public OrderConfirmation createOrder(final @RequestBody OrderDto orderDto) {
        OrderDtoValidator.validateOrder(orderDto);
        final Order order = dtoToDomainMapper.orderDtoToDomain(orderDto);
        return factoryOrderService.createOrder(order);
    }
}