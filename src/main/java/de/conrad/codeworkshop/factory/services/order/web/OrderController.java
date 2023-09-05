package de.conrad.codeworkshop.factory.services.order.web;

import de.conrad.codeworkshop.factory.services.order.business.OrderService;
import de.conrad.codeworkshop.factory.services.order.business.domain.OrderConfirmation;
import de.conrad.codeworkshop.factory.services.order.web.api.OrderDto;
import de.conrad.codeworkshop.factory.services.order.web.mapping.DtoToDomainMapper;
import de.conrad.codeworkshop.factory.services.order.web.validation.OrderDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static de.conrad.codeworkshop.factory.services.order.business.domain.OrderConfirmation.DECLINED_ORDER_CONFIRMATION;

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
        produces = {"application/json"},
        consumes = {"application/json"}
    )
    public OrderConfirmation createOrder(final @RequestBody OrderDto orderDto) {
        Set<FieldError> validationErrors = OrderDtoValidator.validateOrder(orderDto);
        if (!CollectionUtils.isEmpty(validationErrors)) {
            // replace return DECLINED_ORDER_CONFIRMATION with uncommented code to get error messages as response
            // throw new OrderValidationException("The order validation is failed. Please check the errors for details.", validationErrors);
            return DECLINED_ORDER_CONFIRMATION;
        }
        return factoryOrderService.createOrder(dtoToDomainMapper.orderDtoToDomain(orderDto));
    }
}