package de.conrad.codeworkshop.factory.services.factory;

import de.conrad.codeworkshop.factory.services.order.business.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author Andreas Hartmann
 */
@RestController("factoryController")
@RequestMapping("/factory")
// TODO should be removed? FactoryController is not in use
public class FactoryController {

    private final FactoryService factoryService;

    @Autowired
    public FactoryController(final FactoryService factoryService) {
        this.factoryService = factoryService;
    }

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
}
